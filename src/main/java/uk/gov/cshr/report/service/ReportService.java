package uk.gov.cshr.report.service;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.repository.ModuleReportRowRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final LearnerRecordService learnerRecordService;
    private final CivilServantRegistryService civilServantRegistryService;
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final IdentityService identityService;
    private final ModuleReportRowRepository moduleReportRowRepository;

    @Autowired
    public ReportService(LearnerRecordService learnerRecordService,
            CivilServantRegistryService civilServantRegistryService,
            LearningCatalogueService learningCatalogueService,
            ReportRowFactory reportRowFactory,
            IdentityService identityService,
            ModuleReportRowRepository moduleReportRowRepository) {
        this.learnerRecordService = learnerRecordService;
        this.civilServantRegistryService = civilServantRegistryService;
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.identityService = identityService;
        this.moduleReportRowRepository = moduleReportRowRepository;
    }

    public List<BookingReportRow> buildBookingReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {

        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = learnerRecordService.getBookings(from, to);
        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();
        Map<String, Identity> identitiesMap = identityService.getIdentitiesMap();

        for (Booking booking : bookings) {
            if (civilServantMap.containsKey(booking.getLearner())) {
                String eventUid = Paths.get(booking.getEvent()).getFileName().toString();
                Identity identity = identitiesMap.get(booking.getLearner());

                if (eventMap.containsKey(eventUid)) {
                    Optional<CivilServant> civilServant = Optional.ofNullable(civilServantMap.get(booking.getLearner()));
                    Optional<Event> event = Optional.ofNullable(eventMap.get(eventUid));
                    report.add(reportRowFactory.createBookingReportRow(civilServant, event, booking, identity, isProfessionReporter));
                }
            }
        }


        return report;
    }

    public List<ModuleReportRow> buildModuleReport(LocalDate from, LocalDate to, boolean isProfessionReporter) {
        List<ModuleReportRow> report = new ArrayList<>();
        List<ModuleReportRow> moduleReportRows = moduleReportRowRepository.getModuleReportData(from, to, isProfessionReporter);
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();
        
        moduleReportRows.forEach(reportRow -> {
            if (moduleMap.containsKey(reportRow.getModuleId())) {
                report.add(mapDataFromModuleToReportRow(reportRow, moduleMap.get(reportRow.getModuleId())));
            }
        });

        return report;
    }

    private ModuleReportRow mapDataFromModuleToReportRow(ModuleReportRow reportRow, Module module) {
        reportRow.setCourseId(module.getCourse().getId());
        reportRow.setCourseTitle(module.getCourse().getTitle());
        reportRow.setCourseTopicId(module.getCourse().getTopicId());
        reportRow.setModuleId(module.getId());
        reportRow.setModuleTitle(module.getTitle());
        reportRow.setModuleType(module.getType());

        if (module.getAssociatedLearning() != null) {
            reportRow.setPaidFor(module.getAssociatedLearning());
        }

        return reportRow;
    }
}
