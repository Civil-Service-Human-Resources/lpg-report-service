package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.registry.CivilServantRegistryService;

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

    public ReportService(LearnerRecordService learnerRecordService, CivilServantRegistryService civilServantRegistryService, LearningCatalogueService learningCatalogueService, ReportRowFactory reportRowFactory) {
        this.learnerRecordService = learnerRecordService;
        this.civilServantRegistryService = civilServantRegistryService;
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
    }

    public List<BookingReportRow> buildBookingReport(LocalDate from, LocalDate to) {

        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = learnerRecordService.getBookings(from, to);
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();

        for (Booking booking : bookings) {
            if (civilServantMap.containsKey(booking.getLearner())) {
                String eventUid = Paths.get(booking.getEvent()).getFileName().toString();

                Optional<CivilServant> civilServant = Optional.ofNullable(civilServantMap.get(booking.getLearner()));
                Optional.ofNullable(eventMap.get(eventUid)).ifPresent(event -> report.add(reportRowFactory.createBookingReportRow(civilServant, event, booking)));
            }
        }

        return report;
    }

    public List<ModuleReportRow> buildModuleReport(LocalDate from, LocalDate to) {
        List<ModuleRecord> moduleRecords = learnerRecordService.getModules(from, to);
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();

        return buildModuleReport(moduleRecords, civilServantMap, moduleMap);
    }

    public List<ModuleReportRow> buildModuleReport(LocalDate from, LocalDate to, long professionId) {
        List<ModuleRecord> moduleRecords = learnerRecordService.getModules(from, to);
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap(professionId);

        return buildModuleReport(moduleRecords, civilServantMap, moduleMap);
    }

    private List<ModuleReportRow> buildModuleReport(List<ModuleRecord> moduleRecords,
                                                    Map<String, CivilServant> civilServantMap,
                                                    Map<String, Module> moduleMap) {
        List<ModuleReportRow> report = new ArrayList<>();

        for (ModuleRecord moduleRecord : moduleRecords) {
            if (civilServantMap.containsKey(moduleRecord.getLearner())) {

                CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
                Module module = moduleMap.get(moduleRecord.getModuleId());
                report.add(reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord));
            }
        }
        return report;
    }
}
