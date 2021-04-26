package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.LearnerRecordSummary;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class LearnerRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordService.class);

    private final HttpService httpService;
    private final UriBuilderFactory uriBuilderFactory;
    private final URI learnerRecordSummariesUrl;
    private final URI learnerRecordEventsUrl;
    private final String bookingUri;
    private final String moduleRecordUri;
    private final String moduleRecordsForLearnersUrl;
    private final String moduleRecordsForCourseIdsUrl;

    public LearnerRecordService(HttpService httpService,
                                UriBuilderFactory uriBuilderFactory,
                                @Value("${learnerRecord.summariesUrl}") URI learnerRecordSummariesUrl,
                                @Value("${learnerRecord.eventsUrl}") URI learnerRecordEventsUrl,
                                @Value("${learnerRecord.bookingsUrl}") String bookingUri,
                                @Value("${learnerRecord.moduleRecordsUrl}") String moduleRecordUri,
                                @Value("${learnerRecord.moduleRecordsForLearnersUrl}") String moduleRecordsForLearnersUrl,
                                @Value("${learnerRecord.moduleRecordsForCourseIdsUrl}") String moduleRecordsForCourseIdsUrl
    ) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.learnerRecordSummariesUrl = learnerRecordSummariesUrl;
        this.learnerRecordEventsUrl = learnerRecordEventsUrl;
        this.bookingUri = bookingUri;
        this.moduleRecordUri = moduleRecordUri;
        this.moduleRecordsForLearnersUrl = moduleRecordsForLearnersUrl;
        this.moduleRecordsForCourseIdsUrl = moduleRecordsForCourseIdsUrl;
    }

    @PreAuthorize("hasAnyAuthority('ORGANISATION_REPORTER', 'PROFESSION_REPORTER', 'CSHR_REPORTER')")
    public List<LearnerRecordSummary> listRecords() {
        LOGGER.debug("Listing records");
        return httpService.getList(learnerRecordSummariesUrl, LearnerRecordSummary.class);
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public List<LearnerRecordEvent> listEvents() {
        LOGGER.debug("Listing events");

        return httpService.getList(learnerRecordEventsUrl, LearnerRecordEvent.class);
    }

    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        URI uri = uriBuilderFactory.builder(bookingUri)
                .queryParam("from", from)
                .queryParam("to", to)
                .build(new HashMap<>());

        return httpService.getList(uri, Booking.class);
    }

    public List<ModuleRecord> getModules(LocalDate from, LocalDate to) {
        URI uri = uriBuilderFactory.builder(moduleRecordUri)
                .queryParam("from", from)
                .queryParam("to", to)
                .build(new HashMap<>());

        return httpService.getList(uri, ModuleRecord.class);
    }

    public List<ModuleRecord> getModuleRecordsForLearners(LocalDate from, LocalDate to, String learnerIds) {
        URI uri = uriBuilderFactory.builder(moduleRecordsForLearnersUrl)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("learnerIds", learnerIds)
                .build(new HashMap<>());
        return httpService.getList(uri, ModuleRecord.class);
    }

    public List<ModuleRecord> getModulesRecordsForCourseIds(LocalDate from, LocalDate to, String courseIds) {
        URI uri = uriBuilderFactory.builder(moduleRecordsForCourseIdsUrl)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("courseIds", courseIds)
                .build(new HashMap<>());
        return httpService.getList(uri, ModuleRecord.class);
    }
}
