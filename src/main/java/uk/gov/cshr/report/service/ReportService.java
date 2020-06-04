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
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final DbRepository dbRepository;

    @Autowired
    public ReportService(LearningCatalogueService learningCatalogueService,
            ReportRowFactory reportRowFactory,
            DbRepository dbRepository) {
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.dbRepository = dbRepository;
    }

    public List<BookingReportRow> buildBookingReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = dbRepository.getBookings(from, to);
        Map<String, CivilServant> civilServantMap = dbRepository.getCivilServantMap();
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();
        Map<String, Identity> identitiesMap = dbRepository.getIdentitiesMap();

        System.out.println("Civil servant keys: " + civilServantMap.keySet().toString());

        for (Booking booking : bookings) {
            System.out.println("Booking learner id: " + booking.getLearner());
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
        Map<String, Identity> identitiesMap = dbRepository.getIdentitiesMap();
        List<ModuleRecord> moduleRecords = dbRepository.getModuleRecords(from, to);
        Map<String, CivilServant> civilServantMap = dbRepository.getCivilServantMap();
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
