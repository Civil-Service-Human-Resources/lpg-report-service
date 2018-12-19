package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<BookingReportRow> buildBookingReport() {

        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = learnerRecordService.getBookings();
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();

        for (Booking booking : bookings) {
            if (civilServantMap.containsKey(booking.getLearner())) {
                String eventUid = Paths.get(booking.getEvent()).getFileName().toString();

                CivilServant civilServant = civilServantMap.get(booking.getLearner());
                Event event = eventMap.get(eventUid);
                report.add(reportRowFactory.createBookingReportRow(civilServant, event, booking));
            }
        }
        return report;
    }
}