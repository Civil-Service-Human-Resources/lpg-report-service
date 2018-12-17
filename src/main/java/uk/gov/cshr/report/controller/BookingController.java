package uk.gov.cshr.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final ReportService reportService;

    public BookingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "application/csv; charset=utf-8")
    public ResponseEntity<List<BookingReportRow>> generateBookingReport() {
        List<BookingReportRow> bookingReport = reportService.buildBookingReport();

        return ResponseEntity.ok(bookingReport);
    }
}
