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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final LearnerRecordService learnerRecordService;
    private final CivilServantRegistryService civilServantRegistryService;
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final IdentitiesService identitiesService;

    public ReportService(LearnerRecordService learnerRecordService, CivilServantRegistryService civilServantRegistryService,
                         LearningCatalogueService learningCatalogueService, ReportRowFactory reportRowFactory, IdentitiesService identitiesService) {
        this.learnerRecordService = learnerRecordService;
        this.civilServantRegistryService = civilServantRegistryService;
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.identitiesService = identitiesService;
    }

    public List<BookingReportRow> buildBookingReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {

        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = learnerRecordService.getBookings(from, to);
        Map <String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();
        Map<String, Identity> identitiesMap = identitiesService.getIdentities();

        for (Booking booking : bookings) {
            if (civilServantMap.containsKey(booking.getLearner())) {
                String eventUid = booking.getEventUid();
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

        if(civilServantMap.keySet().size() > 0) {
            List<ModuleRecord> moduleRecords = learnerRecordService.getModuleRecordsForLearners(from, to, new ArrayList<>(civilServantMap.keySet()));
            List<String> learnerIds = moduleRecords.stream().map(ModuleRecord::getLearner).toList();

            Map<String, Identity> identitiesMapFetched = identitiesService.getIdentitiesFromUids(learnerIds);
            List<String> courseIds = moduleRecords.stream().map(ModuleRecord::getCourseId).toList();

            Map<String, Module> moduleMap = learningCatalogueService.getModuleMapForCourseIds(courseIds);

            moduleRecords.forEach(moduleRecord -> {
                CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
                Identity identity = identitiesMapFetched.get(moduleRecord.getLearner());
                Module module = moduleMap.get(moduleRecord.getModuleId());
                if (identity != null && civilServant != null) {
                    report.add(reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord, identity, isProfessionReporter));
                }
            });
        }
        return report;
    }

    public List<ModuleReportRow> buildSupplierModuleReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
        List<ModuleReportRow> report = new ArrayList<>();
        //1. Get the Module map for the supplier user from learning catalogue.
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
            List<ModuleRecord> moduleRecords = learnerRecordService.getModulesRecordsForCourseIds(from, to, courseIdsList);
            List<String> learnerIds = moduleRecords.stream().map(ModuleRecord::getLearner).toList();

            if (moduleRecords.size() > 0) {
                Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMapForLearnerIds(learnerIds);
                Map<String, Identity> identitiesMap = identitiesService.getIdentitiesFromUids(learnerIds);

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
