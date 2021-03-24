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
        String civilServantIdentityIds = civilServantMap.keySet()
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));

        if(!civilServantIdentityIds.isEmpty()) {
            //3. Get the modules for civilServantIdentityIds from step 2 and the given duration.
            List<ModuleRecord> moduleRecords = learnerRecordService.getModuleRecordsForLearners(from, to, civilServantIdentityIds);

            //4. Retrieve unique learnerIds from moduleRecords from step 3.
            String learnerIds = moduleRecords
                    .stream()
                    .map(ModuleRecord::getLearner)
                    .filter(s -> s != null && !s.isEmpty())
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.joining(","));

            if(!learnerIds.isEmpty()) {
                //5. Get emailId map for the learnerIds from step 4.
                Map<String, Identity> identitiesMap = identityService.getIdentitiesMapForLearners(learnerIds);

                //6. Retrieve unique courseIds from moduleRecords from step 3.
                String courseIds = moduleRecords
                        .stream()
                        .map(ModuleRecord::getCourseId)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.joining(","));

                //7. Get the Module map for the given courseIds from learning catalogue to get the missing data (paidFor, topicId)
                // and latest module title and courseTitle.
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
            }
        }
        return report;
    }

    public List<ModuleReportRow> buildSupplierModuleReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
        List<ModuleReportRow> report = new ArrayList<>();

        //1. Get the Module map for the supplier user from learning catalogue.
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();

        //2. Get the unique courseIds from moduleMap in step 1
        String courseIds = moduleMap.values()
                .stream()
                .map(m -> m.getCourse().getId())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.joining(","));

        if(!courseIds.isEmpty()) {
            //3. Get the ModuleRecords for the courseIds in step 2 from learner-record Database
            List<ModuleRecord> moduleRecords = learnerRecordService.getModulesRecordsForCourseIds(from, to, courseIds);

            //DELETE IT LATER - 2. Retrieve unique moduleIds from moduleMap from step 1.
            //String moduleIds = String.join(",", moduleMap.keySet());

            //DELETE IT LATER - 3. Get the learner records for the moduleIds from step 1 and for the given duration.
            //List<ModuleRecord> moduleRecords = learnerRecordService.getModulesForModuleIds(from, to, moduleIds);

            //4. Retrieve unique learnerIds from moduleRecords from step 3.
            String learnerIds = moduleRecords
                    .stream()
                    .map(ModuleRecord::getLearner)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.joining(","));

            if(!learnerIds.isEmpty()) {
                //5. Get the civil servants for the learnerId in step 4
                Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMapForLearnerIds(learnerIds);

                //6. Get emailId map for the learnerIds from step 4.
                Map<String, Identity> identitiesMap = identityService.getIdentitiesMapForLearners(learnerIds);

                //7. Prepare the data to create CSV using the data retrieved above.
                moduleRecords.forEach(moduleRecord -> {
                    CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
                    Identity identity = identitiesMap.get(moduleRecord.getLearner());
                    Module module = moduleMap.get(moduleRecord.getModuleId());
                    if (identity != null && civilServant != null) {
                        report.add(reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord, identity, isProfessionReporter));
                    }
                });
            }
        }
        return report;
    }
}
