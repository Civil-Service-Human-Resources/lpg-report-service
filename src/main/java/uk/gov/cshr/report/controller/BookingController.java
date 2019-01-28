package uk.gov.cshr.report.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final ReportService reportService;

    public BookingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "text/csv; charset=utf-8", params = {"from", "to"})
    public ResponseEntity<List<BookingReportRow>> generateBookingReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<BookingReportRow> bookingReport = reportService.buildBookingReport(from, to);
        return ResponseEntity.ok(bookingReport);
    }
}
