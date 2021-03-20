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
        String civilServantIdentityIds = String.join(",", civilServantMap.keySet());

        //3. Get the modules for civilServantIdentityIds from step 2 and the given duration.
        List<ModuleRecord> moduleRecords = learnerRecordService.getModulesForLearners(from, to, civilServantIdentityIds);

        //4. Retrieve unique learnerIds from moduleRecords from step 3.
        String learnerIds = moduleRecords
                .stream()
                .map(ModuleRecord::getLearner)
                .distinct()
                .collect(Collectors.joining(","));

        //5. Get email id of the moduleLearnerIdentityIds from step 4.
        Map<String, Identity> identitiesMap = identityService.getIdentitiesMapForLearners(learnerIds);

        //6. Retrieve unique courseIds from moduleRecords from step 3:
        String courseIds = moduleRecords
                .stream()
                .map(ModuleRecord::getCourseId)
                .distinct()
                .collect(Collectors.joining(","));

        //7. Get the courses for the given courseIds from learning catalogue.
        //Call learning catalogue to get the course map rather then the module map to get the missing data (paidFor, topicId and latest courseTitle).
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMapForCourseIds(courseIds);

        //8. Prepare the data to create CSV using the data retrieved above.
        moduleRecords.forEach(moduleRecord -> {
            CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
            Identity identity = identitiesMap.get(moduleRecord.getLearner());
            Module module = moduleMap.get(moduleRecord.getModuleId());
            if (identity != null && civilServant != null) {
                report.add(reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord, identity, isProfessionReporter));
            }
        });

        /*NOTE: If decision is made to de-couple the Elasticsearch then remove the steps 6, 7 and 8 above and enable the step 9 below
        //9. Prepare the data to create CSV using the data retrieved above.
        moduleRecords.forEach(moduleRecord -> {
            CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
            Identity identity = identitiesMap.get(moduleRecord.getLearner());
            if (identity != null && civilServant != null) {
                report.add(reportRowFactory.createModuleReportRow(civilServant, null, moduleRecord, identity, isProfessionReporter));
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
