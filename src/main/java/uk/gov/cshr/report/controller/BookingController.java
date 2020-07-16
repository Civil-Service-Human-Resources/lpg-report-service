package uk.gov.cshr.report.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final ReportService reportService;

    public BookingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "application/csv; charset=utf-8", params = {"from", "to"})
    public ResponseEntity<List<BookingReportRow>> generateBookingReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, Authentication authentication
    ) throws ExecutionException, InterruptedException {
        LOGGER.info("Generating booking report by report by user with ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal().toString(), from.toString(), to.toString());
        boolean isProfessionReporter = authentication.getAuthorities().contains(new SimpleGrantedAuthority("PROFESSION_REPORTER"));
        List<BookingReportRow> bookingReport = reportService.buildBookingReport(from, to, isProfessionReporter);
        return ResponseEntity.ok(bookingReport);
    }
}
