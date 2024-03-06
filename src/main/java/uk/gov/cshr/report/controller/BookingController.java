package uk.gov.cshr.report.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final ReportService reportService;

    public BookingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "application/csv; charset=utf-8", params = {"from", "to"})
    public ResponseEntity<List<BookingReportRow>> generateBookingReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, Authentication authentication
    ) {
        log.info("Generating booking report for user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        boolean isProfessionReporter = authentication.getAuthorities().contains(new SimpleGrantedAuthority("PROFESSION_REPORTER"));
        List<BookingReportRow> bookingReport = reportService.buildBookingReport(from, to, isProfessionReporter);
        bookingReport.forEach(r -> r.getCourseId());
        log.info("Booking report generated for user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        return ResponseEntity.ok(bookingReport);
    }
}
