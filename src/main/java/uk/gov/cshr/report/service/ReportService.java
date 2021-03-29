package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class ReportService {
    private final LearnerRecordService learnerRecordService;
    private final CivilServantRegistryService civilServantRegistryService;
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final IdentityService identityService;
    private final int backEndCallBatchSize;//value of the civilServantIdentityId is 36 character long plus one comma for separation so total length os 37, total length is 37*50=1850

    public ReportService(LearnerRecordService learnerRecordService, CivilServantRegistryService civilServantRegistryService, LearningCatalogueService learningCatalogueService, ReportRowFactory reportRowFactory, IdentityService identityService) {
        this.learnerRecordService = learnerRecordService;
        this.civilServantRegistryService = civilServantRegistryService;
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.identityService = identityService;
        this.backEndCallBatchSize = 50;
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
        log.info("ReportService.buildModuleReport.Calling civilServantRegistryService.getCivilServantMap()");
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        if(civilServantMap.keySet().size() > 0) {
            //2. Retrieve the unique civilServantIdentityIds from the list of CivilServants from step 1.
            List<String> allCivilServantIdsList = new ArrayList<>(civilServantMap.keySet());
            int totalItems = allCivilServantIdsList.size();
            int remaining = totalItems;
            int startSize = 0;
            int endSize;
            int totalFetched;
            List<ModuleRecord> moduleRecords = new ArrayList<>();
            do {
                if (remaining > backEndCallBatchSize) {
                    endSize = startSize + backEndCallBatchSize;
                } else {
                    endSize = startSize + remaining;
                }
                List<String> subListOfCivilServantIdsList = allCivilServantIdsList.subList(startSize, endSize);
                String civilServantIdentityIds = subListOfCivilServantIdsList
                        .stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(","));
                //3. Get the modules for civilServantIdentityIds from step 2 and the given duration.
                log.info("ReportService.buildModuleReport.Calling learnerRecordService.getModuleRecordsForLearners(from, to, civilServantIdentityIds)");
                List<ModuleRecord> subListModuleRecords = learnerRecordService.getModuleRecordsForLearners(from, to, civilServantIdentityIds);
                moduleRecords.addAll(subListModuleRecords);
                totalFetched = endSize;
                remaining = totalItems - totalFetched;
                startSize = endSize;
            } while (remaining > 0);

            if (moduleRecords.size() > 0) {
                //4. Retrieve unique learnerIds from moduleRecords from step 3.
                List<String> learnerIdsList = moduleRecords
                        .stream()
                        .map(ModuleRecord::getLearner)
                        .filter(s -> s != null && !s.isEmpty())
                        .map(String::trim)
                        .distinct()
                        .collect(Collectors.toList());
                totalItems = learnerIdsList.size();
                remaining = totalItems;
                startSize = 0;
                Map<String, Identity> identitiesMap = new HashMap<>();
                do {
                    if (remaining > backEndCallBatchSize) {
                        endSize = startSize + backEndCallBatchSize;
                    } else {
                        endSize = startSize + remaining;
                    }
                    List<String> subListOfLearnerIdsList = learnerIdsList.subList(startSize, endSize);
                    String learnerIds = subListOfLearnerIdsList
                            .stream()
                            .filter(s -> s != null && !s.isEmpty())
                            .map(String::trim)
                            .distinct()
                            .collect(Collectors.joining(","));
                    //5. Get emailId map for the learnerIds from step 4.
                    log.info("ReportService.buildModuleReport.Calling identityService.getIdentitiesMapForLearners(learnerIds)");
                    Map<String, Identity> identitiesMapFetched = identityService.getIdentitiesMapForLearners(learnerIds);
                    identitiesMap.putAll(identitiesMapFetched);
                    totalFetched = endSize;
                    remaining = totalItems - totalFetched;
                    startSize = endSize;
                } while (remaining > 0);

                //6. Retrieve unique courseIds from moduleRecords from step 3.
                List<String> courseIdsList = moduleRecords
                        .stream()
                        .map(ModuleRecord::getCourseId)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());
                totalItems = courseIdsList.size();
                remaining = totalItems;
                startSize = 0;
                Map<String, Module> moduleMap = new HashMap<>();
                do {
                    if (remaining > backEndCallBatchSize) {
                        endSize = startSize + backEndCallBatchSize;
                    } else {
                        endSize = startSize + remaining;
                    }
                    List<String> subListOfCourseIdsList = courseIdsList.subList(startSize, endSize);
                    String courseIds = subListOfCourseIdsList
                            .stream()
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .distinct()
                            .collect(Collectors.joining(","));
                    //7. Get the Module map for the given courseIds from learning catalogue to get the missing data (paidFor, topicId)
                    // and latest module title and courseTitle.
                    log.info("ReportService.buildModuleReport.Calling learningCatalogueService.getModuleMapForCourseIds(courseIds)");
                    Map<String, Module> moduleMapFetched = learningCatalogueService.getModuleMapForCourseIds(courseIds);
                    moduleMap.putAll(moduleMapFetched);
                    totalFetched = endSize;
                    remaining = totalItems - totalFetched;
                    startSize = endSize;
                } while (remaining > 0);

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
        log.info("ReportService.buildSupplierModuleReport.Calling learningCatalogueService.getModuleMap()");
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();
        //2. Get the unique courseIds from moduleMap in step 1
        List<String> courseIdsList = moduleMap.values()
                .stream()
                .map(m -> m.getCourse().getId())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        if (courseIdsList.size() > 0) {
            int totalItems = courseIdsList.size();
            int remaining = totalItems;
            int startSize = 0;
            int endSize;
            int totalFetched;
            List<ModuleRecord> moduleRecords = new ArrayList<>();
            do {
                if (remaining > backEndCallBatchSize) {
                    endSize = startSize + backEndCallBatchSize;
                } else {
                    endSize = startSize + remaining;
                }
                List<String> subListOfCourseIdsList = courseIdsList.subList(startSize, endSize);
                String courseIds = subListOfCourseIdsList
                        .stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(","));
                //3. Get the ModuleRecords for the courseIds in step 2 from learner-record Database
                log.info("ReportService.buildSupplierModuleReport.Calling learnerRecordService.getModulesRecordsForCourseIds(from, to, courseIds)");
                List<ModuleRecord> moduleRecordsFetched = learnerRecordService.getModulesRecordsForCourseIds(from, to, courseIds);
                moduleRecords.addAll(moduleRecordsFetched);
                totalFetched = endSize;
                remaining = totalItems - totalFetched;
                startSize = endSize;
            } while (remaining > 0);

            if (moduleRecords.size() > 0) {
                //4. Retrieve unique learnerIds from moduleRecords from step 3.
                List<String> learnerIdsList = moduleRecords
                        .stream()
                        .map(ModuleRecord::getLearner)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());
                totalItems = learnerIdsList.size();
                remaining = totalItems;
                startSize = 0;
                Map<String, CivilServant> civilServantMap = new HashMap<>();
                do {
                    if (remaining > backEndCallBatchSize) {
                        endSize = startSize + backEndCallBatchSize;
                    } else {
                        endSize = startSize + remaining;
                    }
                    List<String> subListOfLearnerIdsList = learnerIdsList.subList(startSize, endSize);
                    String learnerIds = subListOfLearnerIdsList
                            .stream()
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining(","));
                    //5. Get the civil servants for the learnerId in step 4
                    log.info("ReportService.buildSupplierModuleReport.Calling civilServantRegistryService.getCivilServantMapForLearnerIds(learnerIds)");
                    Map<String, CivilServant> civilServantMapFetched = civilServantRegistryService.getCivilServantMapForLearnerIds(learnerIds);
                    civilServantMap.putAll(civilServantMapFetched);
                    totalFetched = endSize;
                    remaining = totalItems - totalFetched;
                    startSize = endSize;
                } while (remaining > 0);

                totalItems = learnerIdsList.size();
                remaining = totalItems;
                startSize = 0;
                Map<String, Identity> identitiesMap = new HashMap<>();
                do {
                    if (remaining > backEndCallBatchSize) {
                        endSize = startSize + backEndCallBatchSize;
                    } else {
                        endSize = startSize + remaining;
                    }
                    List<String> subListOfLearnerIdsList = learnerIdsList.subList(startSize, endSize);
                    String learnerIds = subListOfLearnerIdsList
                            .stream()
                            .filter(s -> s != null && !s.isEmpty())
                            .map(String::trim)
                            .distinct()
                            .collect(Collectors.joining(","));
                    //6. Get emailId map for the learnerIds from step 4.
                    log.info("ReportService.buildSupplierModuleReport.Calling identityService.getIdentitiesMapForLearners(learnerIds)");
                    Map<String, Identity> identitiesMapFetched = identityService.getIdentitiesMapForLearners(learnerIds);
                    identitiesMap.putAll(identitiesMapFetched);
                    totalFetched = endSize;
                    remaining = totalItems - totalFetched;
                    startSize = endSize;
                } while (remaining > 0);

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
