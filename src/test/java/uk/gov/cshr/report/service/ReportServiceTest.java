package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    private LearnerRecordService learnerRecordService;

    @Mock
    private CivilServantRegistryService civilServantRegistryService;

    @Mock
    private LearningCatalogueService learningCatalogueService;

    @Mock
    private IdentitiesService identitiesService;

    @Mock
    private ReportRowFactory reportRowFactory;


    private ReportService reportService;

    @BeforeEach
    public void setUp() throws Exception {
        reportService = new ReportService(learnerRecordService, civilServantRegistryService, learningCatalogueService,
                reportRowFactory, identitiesService);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnBookingReport() {
        Booking booking1 = new Booking();
        booking1.setEventUid("event1");
        booking1.setLearner("learner1");

        Booking booking2 = new Booking();
        booking2.setEventUid("event2");
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
        identity.setUid(UUID.randomUUID().toString());
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
    public void testBuildModuleReportShouldReturnCorrectRowsForCSVReport() {

        List<String> learners = Arrays.asList("learner1", "learner2", "learner3");
        List<String> courseIds = List.of("courseId1", "courseId2", "courseId3");

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId("learner1");
        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId("learner2");
        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId("learner3");
        ImmutableMap<String, CivilServant> civilServants = ImmutableMap.of(
                "learner1", civilServant1,
                "learner2", civilServant2,
                "learner3", civilServant3
        );
        when(civilServantRegistryService.getCivilServantMap()).thenReturn(civilServants);

        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        ModuleRecord moduleRecord1 = new ModuleRecord();
        moduleRecord1.setModuleId("moduleId1");
        moduleRecord1.setLearner("learner1");
        moduleRecord1.setCourseId("courseId1");
        ModuleRecord moduleRecord2 = new ModuleRecord();
        moduleRecord2.setModuleId("moduleId2");
        moduleRecord2.setLearner("learner2");
        moduleRecord2.setCourseId("courseId2");
        ModuleRecord moduleRecord3 = new ModuleRecord();
        moduleRecord3.setModuleId("moduleId3");
        moduleRecord3.setLearner("learner3");
        moduleRecord3.setCourseId("courseId3");
        List<ModuleRecord> moduleRecords = Arrays.asList(moduleRecord1, moduleRecord2, moduleRecord3);
        when(learnerRecordService.getModuleRecordsForLearners(from, to, learners)).thenReturn(moduleRecords);

        Identity identity1 = new Identity();
        identity1.setUid("learner1");
        identity1.setUsername("learner1@example.com");
        Identity identity2 = new Identity();
        identity2.setUid("learner2");
        identity2.setUsername("learner2@example.com");
        Identity identity3 = new Identity();
        identity3.setUid("learner3");
        identity3.setUsername("learner3@example.com");
        ImmutableMap<String, Identity> identities = ImmutableMap.of(
                identity1.getUid(), identity1,
                identity2.getUid(), identity2,
                identity3.getUid(), identity3
        );
        when(identitiesService.getIdentitiesFromUids(learners)).thenReturn(identities);

        Module module1 = new Module();
        module1.setId("moduleId1");
        Course course1 = new Course();
        course1.setId("courseId1");
        module1.setCourse(course1);
        Module module2 = new Module();
        module2.setId("moduleId2");
        Course course2 = new Course();
        course2.setId("courseId2");
        module2.setCourse(course2);
        Module module3 = new Module();
        module3.setId("moduleId3");
        Course course3 = new Course();
        course3.setId("courseId3");
        module3.setCourse(course3);
        ImmutableMap<String, Module> modules = ImmutableMap.of(
                "moduleId1", module1,
                "moduleId2", module2,
                "moduleId3", module3
        );
        when(learningCatalogueService.getModuleMapForCourseIds(courseIds)).thenReturn(modules);

        ReportRowFactory reportRowFactory1 = new ReportRowFactory();

        ModuleReportRow moduleReportRow1 = reportRowFactory1.createModuleReportRow(civilServant1, module1, moduleRecord1, identity1, false);
        when(reportRowFactory.createModuleReportRow(civilServant1, module1, moduleRecord1, identity1, false)).thenReturn(moduleReportRow1);
        ModuleReportRow moduleReportRow2 = reportRowFactory1.createModuleReportRow(civilServant2, module2, moduleRecord2, identity2, false);
        when(reportRowFactory.createModuleReportRow(civilServant2, module2, moduleRecord2, identity2, false)).thenReturn(moduleReportRow2);
        ModuleReportRow moduleReportRow3 = reportRowFactory1.createModuleReportRow(civilServant3, module3, moduleRecord3, identity3, false);
        when(reportRowFactory.createModuleReportRow(civilServant3, module3, moduleRecord3, identity3, false)).thenReturn(moduleReportRow3);

        List<ModuleReportRow> moduleReportRows = Arrays.asList(moduleReportRow1, moduleReportRow2, moduleReportRow3);

        List<ModuleReportRow> result = reportService.buildModuleReport(from, to, false);

        assertEquals(moduleReportRows, result);
    }
}
