package uk.gov.cshr.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final ReportService reportService;

    public BookingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "application/csv; charset=utf-8", params = {"from", "to"})
    public ResponseEntity<List<BookingReportRow>> generateBookingReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestHeader(required = false) Integer organisationId,
            Authentication authentication
    ) {
        log.info("Generating booking report for user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        boolean isCshrReporter = authentication.getAuthorities().contains(new SimpleGrantedAuthority("CSHR_REPORTER"));
        organisationId = isCshrReporter ? null : organisationId;
        List<BookingReportRow> bookingReport = reportService.buildBookingReport(from, to, organisationId);
        log.info("Booking report generated for user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        return ResponseEntity.ok(bookingReport);
    }
}
