package uk.gov.cshr.report.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cshr.report.reports.CourseReportRow;
import uk.gov.cshr.report.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CourseController {

    private final ReportService reportService;

    public CourseController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/mandatory-courses", produces = "application/csv")
    public ResponseEntity<List<CourseReportRow>> generateMandatoryCourseReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication
    ) {
        boolean isProfessionReporter = authentication.getAuthorities().contains(new SimpleGrantedAuthority("PROFESSION_REPORTER"));
        List<CourseReportRow> report = reportService.buildMandatoryCourseReport(from, to, isProfessionReporter);
        return ResponseEntity.ok(report);
    }
}
