package uk.gov.cshr.report.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.ReportService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/modules")
public class ModuleController {
    private final ReportService reportService;

    public ModuleController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "application/csv")
    public ResponseEntity<List<ModuleReportRow>> generateModuleReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication
    ) {
        log.info("Generating learner record report by user with ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        boolean isProfessionReporter = authentication.getAuthorities().contains(new SimpleGrantedAuthority("PROFESSION_REPORTER"));
        List<ModuleReportRow> report = reportService.buildModuleReport(from, to, isProfessionReporter);

        return ResponseEntity.ok(report);
    }
}
