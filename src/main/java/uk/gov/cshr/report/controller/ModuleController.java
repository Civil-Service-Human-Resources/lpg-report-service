package uk.gov.cshr.report.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cshr.report.mapping.RoleMapping;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.ReportService;
import uk.gov.cshr.report.service.registry.CivilServantRegistryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ReportService reportService;
    private final CivilServantRegistryService civilServantRegistryService;

    public ModuleController(ReportService reportService, CivilServantRegistryService civilServantRegistryService) {
        this.reportService = reportService;
        this.civilServantRegistryService = civilServantRegistryService;
    }

    @GetMapping(produces = "text/csv")
    public ResponseEntity<List<ModuleReportRow>> generateModuleReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {

        List<ModuleReportRow> report = reportService.buildModuleReport(from, to);

        return ResponseEntity.ok(report);
    }

    @RoleMapping("CSHR_REPORTER")
    @GetMapping(produces = "text/csv", params = "professionId")
    public ResponseEntity<List<ModuleReportRow>> generateModuleReportForProfession(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam long professionId
    ) {
        List<ModuleReportRow> report = reportService.buildModuleReport(from, to, professionId);

        return ResponseEntity.ok(report);
    }

    @RoleMapping("PROFESSION_REPORTER")
    @GetMapping(produces = "text/csv", params = "professionId")
    public ResponseEntity<List<ModuleReportRow>> generateModuleReportForProfessionReporter(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam long professionId
    ) {
        if (civilServantRegistryService.userHasProfession(professionId)) {
            return this.generateModuleReportForProfession(from, to, professionId);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping(produces = "text/csv", params = "professionId")
    public ResponseEntity<List<ModuleReportRow>> generateModuleReportForbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
