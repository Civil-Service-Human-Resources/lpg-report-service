package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.CourseRecord;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.CourseReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {
    private final LearnerRecordService learnerRecordService;
    private final CivilServantRegistryService civilServantRegistryService;
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final IdentityService identityService;

    public ReportService(LearnerRecordService learnerRecordService, CivilServantRegistryService civilServantRegistryService, LearningCatalogueService learningCatalogueService, ReportRowFactory reportRowFactory, IdentityService identityService) {
        this.learnerRecordService = learnerRecordService;
        this.civilServantRegistryService = civilServantRegistryService;
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.identityService = identityService;
    }

    public List<BookingReportRow> buildBookingReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {

        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = learnerRecordService.getBookings(from, to);
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();
        Map<String, Identity> identitiesMap = identityService.getIdentitiesMap();

        for (Booking booking : bookings) {
            if (civilServantMap.containsKey(booking.getLearner())) {
                String eventUid = Paths.get(booking.getEvent()).getFileName().toString();
                Identity identity = identitiesMap.get(booking.getLearner());

                if (eventMap.containsKey(eventUid)) {
                    Optional<CivilServant> civilServant = Optional.ofNullable(civilServantMap.get(booking.getLearner()));
                    Optional<Event> event = Optional.ofNullable(eventMap.get(eventUid));
                    report.add(reportRowFactory.createBookingReportRow(civilServant, event, booking, identity, isProfessionReporter));
                }
            }
        }


        return report;
    }

    public List<ModuleReportRow> buildModuleReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
        List<ModuleReportRow> report = new ArrayList<>();

        Map<String, Identity> identitiesMap = identityService.getIdentitiesMap();
        List<ModuleRecord> moduleRecords = learnerRecordService.getModules(from, to);
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();

        for (ModuleRecord moduleRecord : moduleRecords) {
            if (civilServantMap.containsKey(moduleRecord.getLearner())) {

                CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
                Identity identity = identitiesMap.get(moduleRecord.getLearner());

                if (moduleMap.containsKey(moduleRecord.getModuleId())) {
                    Module module = moduleMap.get(moduleRecord.getModuleId());
                    if (module != null && identity != null && civilServant != null) {
                        report.add(reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord, identity, isProfessionReporter));
                    }
                }
            }
        }
        return report;
    }

    public List<CourseReportRow> buildMandatoryCourseReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
        List<CourseReportRow> report = new ArrayList<>();

        Map<String, Identity> identitiesMap = identityService.getIdentitiesMap();
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();

        List<CourseRecord> courseRecords = learnerRecordService.getCourses(from, to);
        Map<String, Course> courseMap = learningCatalogueService.getMandatoryCourses();

        for (CourseRecord courseRecord : courseRecords) {
            if (civilServantMap.containsKey(courseRecord.getLearner())) {

                CivilServant civilServant = civilServantMap.get(courseRecord.getLearner());
                Identity identity = identitiesMap.get(courseRecord.getLearner());

                if (courseMap.containsKey(courseRecord.getCourseId())) {
                    Course course = courseMap.get(courseRecord.getCourseId());
                    if (course != null && identity != null && civilServant != null) {
                        report.add(reportRowFactory.createCourseReportRow(civilServant, course, courseRecord, identity, isProfessionReporter, true));
                    }
                }
            }
        }
        return report;
    }
}
