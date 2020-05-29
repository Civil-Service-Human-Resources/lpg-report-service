package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;

import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.repository.ModuleReportRowRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
    private static final String MODULE_1 = "module1";
    private static final String COURSE_1 = "course1";
    private static final String LEARNER_1 = "learner1";
    private static final String EVENT_1 = "event1";
    private static final String EMAIL_1 = "test1@example.com";
    private static final String MODULE_2 = "module2";
    private static final String COURSE_2 = "course2";
    private static final String LEARNER_2 = "learner2";
    private static final String EMAIL_2 = "test2@example.com";
    private static final String EVENT_2 = "event2";
    private static final String LEARNER_3 = "learner3";
    private static final String DATE_FROM = "2018-01-01";
    private static final String DATE_TO = "2018-01-31";
    
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

    @Mock
    private ModuleReportRowRepository moduleReportRowRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnBookingReport() {
        Booking booking1 = new Booking();
        booking1.setEvent(EVENT_1);
        booking1.setLearner(LEARNER_1);

        Booking booking2 = new Booking();
        booking2.setEvent(EVENT_2);
        booking2.setLearner(LEARNER_2);

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId(LEARNER_1);

        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId(LEARNER_2);

        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId(LEARNER_3);

        Event event = new Event();
        event.setId(EVENT_1);

        Identity identity = new Identity();
        identity.setUsername(EMAIL_1);

        LocalDate from = LocalDate.parse(DATE_FROM);
        LocalDate to = LocalDate.parse(DATE_TO);

        when(learnerRecordService.getBookings(from, to)).thenReturn(Arrays.asList(booking1, booking2));
        when(civilServantRegistryService.getCivilServantMap()).thenReturn(ImmutableMap.of(
                LEARNER_1, civilServant1,
                LEARNER_3, civilServant3
        ));
        when(learningCatalogueService.getEventMap()).thenReturn(ImmutableMap.of(EVENT_1, event));

        BookingReportRow reportRow = new BookingReportRow();
        when(reportRowFactory.createBookingReportRow(any(), any(), any(), any(), anyBoolean())).thenReturn(reportRow);

        List<BookingReportRow> result = reportService.buildBookingReport(from, to, false);

        assertEquals(Collections.singletonList(reportRow), result);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnModuleReport() {
        ModuleReportRow moduleReportRow1 = new ModuleReportRow();
        moduleReportRow1.setModuleId(MODULE_1);
        moduleReportRow1.setLearnerId(LEARNER_1);
        moduleReportRow1.setEmail(EMAIL_1);

        ModuleReportRow moduleReportRow2 = new ModuleReportRow();
        moduleReportRow2.setModuleId(MODULE_2);
        moduleReportRow2.setLearnerId(LEARNER_2);
        moduleReportRow2.setEmail(EMAIL_2);

        List<ModuleReportRow> moduleReportRows = ImmutableList.of(moduleReportRow1, moduleReportRow2);

        Course course1 = new Course();
        course1.setId(COURSE_1);
        Module module1 = new Module();
        module1.setId(MODULE_1);
        module1.setCourse(course1);

        Course course2 = new Course();
        course2.setId(COURSE_2);
        Module module2 = new Module();
        module2.setId(MODULE_2);
        module2.setCourse(course2);

        LocalDate from = LocalDate.parse(DATE_FROM);
        LocalDate to = LocalDate.parse(DATE_TO);

        when(moduleReportRowRepository.getModuleReportData(any(LocalDate.class), anyBoolean()))
            .thenReturn(moduleReportRows);
        when(learningCatalogueService.getModuleMap())
            .thenReturn(ImmutableMap.of(module1.getId(), module1, module2.getId(), module2));

        List<ModuleReportRow> result = reportService.buildModuleReport(from, to, true);

        assertEquals(result.get(0).getModuleId(), MODULE_1);
        assertEquals(result.get(0).getLearnerId(), LEARNER_1);
        assertEquals(result.get(0).getEmail(), EMAIL_1);
        assertEquals(result.get(0).getCourseId(), COURSE_1);
        assertEquals(result.get(1).getModuleId(), MODULE_2);
        assertEquals(result.get(1).getLearnerId(), LEARNER_2);
        assertEquals(result.get(1).getEmail(), EMAIL_2);
        assertEquals(result.get(1).getCourseId(), COURSE_2);
    }
}