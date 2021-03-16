package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

        //1. Get Map of CivilServants.
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();

        //2. Retrieve the unique civilServantIdentityIds from the list of CivilServants from step 1.
        List<String> civilServantIdentityIds = new ArrayList<>(civilServantMap.keySet());

        //3. Get the modules for civilServantIdentityIds from step 2 and the given duration.
        List<ModuleRecord> moduleRecords = learnerRecordService.getModulesForLearners(from, to, civilServantIdentityIds);

        //4. Retrieve unique learnerIds from moduleRecords from step 3.
        List<String> learnerIds = moduleRecords
                .stream()
                .map(ModuleRecord::getLearner)
                .distinct()
                .collect(Collectors.toList());

        //5. Get email id of the moduleLearnerIdentityIds from the identity service.
        Map<String, Identity> identitiesMap = identityService.getIdentitiesMapForLearners(learnerIds);

        //6. Prepare the data to create CSV using the data retrieved above.
        moduleRecords.forEach(m -> {
            CivilServant civilServant = civilServantMap.get(m.getLearner());
            Identity identity = identitiesMap.get(m.getLearner());
            if (identity != null && civilServant != null) {
                report.add(reportRowFactory.createModuleReportRowNew(civilServant, m, identity, isProfessionReporter));
            }
        });

        /*
        7. Call learning catalogue to get the course map rather then the module map to get the missing data (paidFor, topicId and latest course title). Ensure that the max pagination size of 10000 is taken care.
            7.a. Retrieve unique course ids from moduleRecords from step 3:
                Set<String> courseIds =  moduleRecords.getCourseIds;
            7.b. Get the courses for the given courseIds from learning catalogue.
                This requires two new method implementation in learning-catalogue service.
        */
        //Map<String, Course> courseMap = learningCatalogueService.getCourseMapForCourseIds(List<String> courseIds)
        //Map<String, Module> moduleMap = learningCatalogueService.getModuleMapForModuleIds();

        /*
        8. Populate the courseTitle, courseTopicId and paidFor using elastic data if the exists in elastic
        report.forEach(r -> {
            Module module = moduleMap.get(r.getModuleId);
            if (module != null) {
                reportRow.setCourseTitle(module.getCourse().getCourseTitle);
                reportRow.setCourseTopicId(module.getCourse().getTopicId());
                reportRow.setPaidFor(module.getAssociatedLearning());
            }
        });

        */

        return report;
    }

    public List<ModuleReportRow> buildSupplierModuleReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
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
}
