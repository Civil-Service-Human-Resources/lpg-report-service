package uk.gov.cshr.report.service;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.enums.ReporterRole;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.repository.DbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final LearningCatalogueService learningCatalogueService;
    private final ReportRowFactory reportRowFactory;
    private final DbRepository dbRepository;

    @Autowired
    public ReportService(LearningCatalogueService learningCatalogueService,
            ReportRowFactory reportRowFactory,
            DbRepository dbRepository) {
        this.learningCatalogueService = learningCatalogueService;
        this.reportRowFactory = reportRowFactory;
        this.dbRepository = dbRepository;
    }

    public List<BookingReportRow> buildBookingReport(LocalDate from, LocalDate to, Authentication authentication) {
        List<BookingReportRow> report = new ArrayList<>();

        List<Booking> bookings = dbRepository.getBookings(from, to);
        Map<String, CivilServant> civilServantMap = fetchCivilServants(authentication);
        Map<String, Event> eventMap = learningCatalogueService.getEventMap();
        Map<String, Identity> identitiesMap = dbRepository.getIdentitiesMap();

        for (Booking booking : bookings) {
            if (civilServantMap.containsKey(booking.getLearner())) {
                String eventUid = Paths.get(booking.getEvent()).getFileName().toString();
                Identity identity = identitiesMap.get(booking.getLearner());

                if (eventMap.containsKey(eventUid)) {
                    Optional<CivilServant> civilServant = Optional.ofNullable(civilServantMap.get(booking.getLearner()));
                    Optional<Event> event = Optional.ofNullable(eventMap.get(eventUid));
                    report.add(reportRowFactory.createBookingReportRow(civilServant, event, booking, identity, isProfessionReporter(authentication)));
                }
            }
        }
        
        return report;
    }

    public List<ModuleReportRow> buildModuleReport(LocalDate from, LocalDate to, Authentication authentication) {
        List<ModuleReportRow> report = new ArrayList<>();
        Map<String, Identity> identitiesMap = dbRepository.getIdentitiesMap();
        List<ModuleRecord> moduleRecords = dbRepository.getModuleRecords(from, to);
        Map<String, CivilServant> civilServantMap = fetchCivilServants(authentication);
        Map<String, Module> moduleMap = learningCatalogueService.getModuleMap();

        for (ModuleRecord moduleRecord : moduleRecords) {
            if (civilServantMap.containsKey(moduleRecord.getLearner())) {

                CivilServant civilServant = civilServantMap.get(moduleRecord.getLearner());
                Identity identity = identitiesMap.get(moduleRecord.getLearner());

                if (moduleMap.containsKey(moduleRecord.getModuleId())) {
                    Module module = moduleMap.get(moduleRecord.getModuleId());
                    if (module != null && identity != null && civilServant != null) {
                        report.add(reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord, identity, isProfessionReporter(authentication)));
                    }
                }
            }
        }

        return report;
    }

    private Map<String, CivilServant> fetchCivilServants(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities =  authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(ReporterRole.ORGANIZATION_REPORTER.getValue()))) {
            return dbRepository.getAllCivilServantsByOrganisation((String) authentication.getPrincipal());
        } else if (authorities.contains(new SimpleGrantedAuthority(ReporterRole.PROFESSION_REPORTER.getValue()))) {
            return dbRepository.getAllCivilServantsByProfession((String) authentication.getPrincipal());
        } else if (authorities.contains(new SimpleGrantedAuthority(ReporterRole.CSHR_REPORTER.getValue()))) {
            return dbRepository.getAllCivilServants();
        }

        throw new IllegalArgumentException("No valid report roles present.");
    }

    private boolean isProfessionReporter(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(ReporterRole.PROFESSION_REPORTER.getValue()));
    }
}
