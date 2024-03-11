package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.learnerRecord.ILearnerRecordClient;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class LearnerRecordService {

    private final HttpService httpService;
    private final UriBuilderFactory uriBuilderFactory;
    private final String moduleRecordUri;

    private ILearnerRecordClient learnerRecordClient;

    public LearnerRecordService(HttpService httpService,
                                UriBuilderFactory uriBuilderFactory,
                                @Value("${learnerRecord.moduleRecordsUrl}") String moduleRecordUri,
                                ILearnerRecordClient learnerRecordClient
    ) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.moduleRecordUri = moduleRecordUri;
        this.learnerRecordClient = learnerRecordClient;
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public List<LearnerRecordEvent> listEvents() {
        return learnerRecordClient.getLearnerRecordEvents();
    }

    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        return learnerRecordClient.getBookings(from, to);
    }

    public List<ModuleRecord> getModules(LocalDate from, LocalDate to) {
        URI uri = uriBuilderFactory.builder(moduleRecordUri)
                .queryParam("from", from)
                .queryParam("to", to)
                .build(new HashMap<>());

        return httpService.getList(uri, ModuleRecord.class);
    }

    public List<ModuleRecord> getModuleRecordsForLearners(LocalDate from, LocalDate to, String learnerIds) {
        return learnerRecordClient.getModuleRecordsForDateRangeAndLearnerIds(from, to, learnerIds);
    }

    public List<ModuleRecord> getModulesRecordsForCourseIds(LocalDate from, LocalDate to, String courseIds) {
        return learnerRecordClient.getModuleRecordsForCourseIds(from, to, courseIds);
    }
}
