package uk.gov.cshr.report.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.repository.DbRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
    private LearningCatalogueService learningCatalogueService;

    @Mock
    private DbRepository dbRepository;

    private ReportService reportService;

    @Before
    public void initialize() {
        reportService = new ReportService(learningCatalogueService,
            new ReportRowFactory(),
            dbRepository);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnBookingReport() {
        Booking booking1 = new Booking();
        booking1.setEvent(EVENT_1);
        booking1.setLearner(LEARNER_1);
        booking1.setStatus(BookingStatus.CONFIRMED);

        Booking booking2 = new Booking();
        booking2.setEvent(EVENT_2);
        booking2.setLearner(LEARNER_2);
        booking2.setStatus(BookingStatus.CONFIRMED);

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId(LEARNER_1);

        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId(LEARNER_2);

        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId(LEARNER_3);

        Course course1 = new Course();
        course1.setId(COURSE_1);
        Module module1 = new Module();
        module1.setId(MODULE_1);
        module1.setCourse(course1);
        module1.setRequired(true);

        Event event = new Event();
        event.setId(EVENT_1);
        event.setModule(module1);

        Identity identity = new Identity();
        identity.setUsername(EMAIL_1);
        identity.setUid(LEARNER_1);

        LocalDate from = LocalDate.parse(DATE_FROM);
        LocalDate to = LocalDate.parse(DATE_TO);

        when(dbRepository.getIdentitiesMap())
            .thenReturn(ImmutableMap.of(identity.getUid(), identity));
        when(dbRepository.getBookings(from, to)).thenReturn(Arrays.asList(booking1, booking2));
        when(dbRepository.getCivilServantMap()).thenReturn(ImmutableMap.of(
                LEARNER_1, civilServant1,
                LEARNER_3, civilServant3
        ));
        when(learningCatalogueService.getEventMap()).thenReturn(ImmutableMap.of(EVENT_1, event));

        List<BookingReportRow> result = reportService.buildBookingReport(from, to, false);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getEventID(), EVENT_1);
        assertEquals(result.get(0).getModuleId(), MODULE_1);
        assertEquals(result.get(0).getLearnerId(), LEARNER_1);
        assertEquals(result.get(0).getEmail(), EMAIL_1);
        assertEquals(result.get(0).getCourseId(), COURSE_1);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnModuleReport() {
        Identity identity1 = new Identity();
        identity1.setUsername(EMAIL_1);
        identity1.setUid(LEARNER_1);
        Identity identity2 = new Identity();
        identity2.setUsername(EMAIL_2);
        identity2.setUid(LEARNER_2);

        ModuleRecord moduleRecord1 = new ModuleRecord();
        moduleRecord1.setLearner(LEARNER_1);
        moduleRecord1.setModuleId(MODULE_1);
        ModuleRecord moduleRecord2 = new ModuleRecord();
        moduleRecord2.setLearner(LEARNER_2);
        moduleRecord2.setModuleId(MODULE_2);

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

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId(LEARNER_1);
        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId(LEARNER_2);

        LocalDate from = LocalDate.parse(DATE_FROM);
        LocalDate to = LocalDate.parse(DATE_TO);

        when(dbRepository.getIdentitiesMap())
            .thenReturn(ImmutableMap.of(identity1.getUid(), identity1, identity2.getUid(), identity2));
        when(dbRepository.getModuleRecords(from, to))
            .thenReturn(ImmutableList.of(moduleRecord1, moduleRecord2));
        when(dbRepository.getCivilServantMap())
            .thenReturn(ImmutableMap.of(civilServant1.getId(), civilServant1, civilServant2.getId(), civilServant2));
        when(learningCatalogueService.getModuleMap())
            .thenReturn(ImmutableMap.of(module1.getId(), module1, module2.getId(), module2));

        List<ModuleReportRow> result = reportService.buildModuleReport(from, to, false);

        assertEquals(result.size(), 2);
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