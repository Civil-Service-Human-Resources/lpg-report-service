package uk.gov.cshr.report.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.ReportService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
        boolean isProfessionReporter = authentication.getAuthorities().contains(new SimpleGrantedAuthority("PROFESSION_REPORTER"));
        boolean supplierRole = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.contains("SUPPLIER"));
        List<ModuleReportRow> report;
        if(supplierRole) {
            log.info("Generating learner record module report for supplier reporting user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
            report = reportService.buildSupplierModuleReport(from, to, isProfessionReporter);
            log.info("Learner record module report generated for supplier reporting user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        } else {
            log.info("Generating learner record module report for reporting user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
            report = reportService.buildModuleReport(from, to, isProfessionReporter);
            log.info("Learner record module report generated for reporting user ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal(), from, to);
        }
        return ResponseEntity.ok(report);
    }
}
