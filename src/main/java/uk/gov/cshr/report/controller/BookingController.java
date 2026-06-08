package uk.gov.cshr.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;
import uk.gov.cshr.report.service.auth.IUserAuthService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final ReportService reportService;
    private final IUserAuthService userAuthService;

    public BookingController(ReportService reportService, IUserAuthService userAuthService) {
        this.reportService = reportService;
        this.userAuthService = userAuthService;
    }

    @GetMapping(produces = "application/csv; charset=utf-8", params = {"from", "to"})
    public ResponseEntity<List<BookingReportRow>> generateBookingReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestHeader(required = false) Integer organisationId
    ) {
        log.info("Generating booking report for user ID \"{}\", from \"{}\" to \"{}\"", userAuthService.getUsername(), from, to);
        boolean isCshrReporter = userAuthService.userHasRole("CSHR_REPORTER");
        organisationId = isCshrReporter ? null : organisationId;
        List<BookingReportRow> bookingReport = reportService.buildBookingReport(from, to, organisationId);
        log.info("Booking report generated for user ID \"{}\", from \"{}\" to \"{}\"", userAuthService.getUsername(), from, to);
        return ResponseEntity.ok(bookingReport);
    }
}
