package uk.gov.cshr.report.service;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.repository.DbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final LearnerRecordService learnerRecordService;
    private final CivilServantRegistryService civilServantRegistryService;
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final IdentityService identityService;
    private final DbRepository dbRepository;

    @Autowired
    public ReportService(LearnerRecordService learnerRecordService,
            CivilServantRegistryService civilServantRegistryService,
            LearningCatalogueService learningCatalogueService,
            ReportRowFactory reportRowFactory,
            IdentityService identityService,
            DbRepository dbRepository) {
        this.learnerRecordService = learnerRecordService;
        this.civilServantRegistryService = civilServantRegistryService;
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.identityService = identityService;
        this.dbRepository = dbRepository;
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
        long start = System.currentTimeMillis();
        Map<String, Identity> identitiesMap = dbRepository.getIdentities();
        long end = System.currentTimeMillis();
        System.out.println("Identities fetch time: " + (end - start) + " milliseconds");
        start = System.currentTimeMillis();
        List<ModuleRecord> moduleRecords = dbRepository.getModuleRecords(from, to);
        end = System.currentTimeMillis();
        System.out.println("Module records fetch time: " + (end - start) + " milliseconds");
        start = System.currentTimeMillis();
        Map<String, CivilServant> civilServantMap = dbRepository.getCivilServants();
        end = System.currentTimeMillis();
        System.out.println("Civil servants fetch time: " + (end - start) + " milliseconds");
        start = System.currentTimeMillis();
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();
        end = System.currentTimeMillis();
        System.out.println("Modules fetch time: " + (end - start) + " milliseconds");

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
