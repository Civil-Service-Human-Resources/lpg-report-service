package uk.gov.cshr.report.controller;

import java.time.LocalDate;
import java.util.List;

import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.ReportService;
import uk.gov.cshr.report.validators.ReportRoleValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleController.class);

    private final ReportService reportService;

    public ModuleController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = "application/csv")
    public ResponseEntity<List<ModuleReportRow>> generateModuleReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication) {
        ReportRoleValidator.validate(authentication.getAuthorities());
        LOGGER.info("Generating learner record report by user with ID \"{}\", from \"{}\" to \"{}\"", authentication.getPrincipal().toString(), from.toString(), to.toString());
        List<ModuleReportRow> report = reportService.buildModuleReport(from, to, authentication);

        return ResponseEntity.ok(report);
    }
}
