package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;
import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.CourseRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.CourseReportRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
    @Mock
    private LearnerRecordService learnerRecordService;

    @Mock
    private CivilServantRegistryService civilServantRegistryService;

    @Mock
    private LearningCatalogueService learningCatalogueService;

    @Mock
    private IdentityService identityService;

    @Mock
    private ReportRowFactory reportRowFactory;

    @InjectMocks
    private ReportService reportService;

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnBookingReport() {
        Booking booking1 = new Booking();
        booking1.setEvent("event1");
        booking1.setLearner("learner1");

        Booking booking2 = new Booking();
        booking2.setEvent("event2");
        booking2.setLearner("learner2");

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId("learner1");

        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId("learner2");

        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId("learner3");

        Event event = new Event();
        event.setId("event1");

        Identity identity = new Identity();
        identity.setUsername("test@example.com");

        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        when(learnerRecordService.getBookings(from, to)).thenReturn(Arrays.asList(booking1, booking2));
        when(civilServantRegistryService.getCivilServantMap()).thenReturn(ImmutableMap.of(
                "learner1", civilServant1,
                "learner3", civilServant3
        ));
        when(learningCatalogueService.getEventMap()).thenReturn(ImmutableMap.of("event1", event));

        BookingReportRow reportRow = new BookingReportRow();
        when(reportRowFactory.createBookingReportRow(any(), any(), any(), any(), anyBoolean())).thenReturn(reportRow);

        List<BookingReportRow> result = reportService.buildBookingReport(from, to, false);

        assertEquals(Collections.singletonList(reportRow), result);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnMandatoryCourseReport() {
        CourseRecord  courseRecord = new CourseRecord();
        courseRecord.setCourseId("courseId");
        courseRecord.setLearner("learner1");
        List<CourseRecord> courseRecordList = new ArrayList<>();
        courseRecordList.add(courseRecord);

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId("learner1");

        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId("learner2");

        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId("learner3");

        Course course = new Course();
        course.setId("courseId");

        Identity identity = new Identity();
        identity.setUsername("test@example.com");

        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2020-01-31");

        when(identityService.getIdentitiesMap()).thenReturn(ImmutableMap.of(
                "learner1", identity));

        when(civilServantRegistryService.getCivilServantMap()).thenReturn(ImmutableMap.of(
                "learner1", civilServant1,
                "learner3", civilServant3
        ));

        when(learnerRecordService.getCourses(from, to)).thenReturn(courseRecordList);
        when(learningCatalogueService.getMandatoryCourses()).thenReturn(ImmutableMap.of("courseId", course));

        CourseReportRow courseReportRow = new CourseReportRow();
        when(reportRowFactory.createCourseReportRow(any(), any(), any(), any(), anyBoolean(), anyBoolean())).thenReturn(courseReportRow);

        List<CourseReportRow> result = reportService.buildMandatoryCourseReport(from, to, false);

        assertEquals(Collections.singletonList(courseReportRow), result);
    }

}