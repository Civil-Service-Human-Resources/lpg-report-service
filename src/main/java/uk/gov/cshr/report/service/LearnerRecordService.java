package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.LearnerRecordEvents;
import uk.gov.cshr.report.domain.LearnerRecordSummary;
import uk.gov.cshr.report.domain.learnerrecord.Booking;

import java.net.URI;
import java.util.List;

@Service
public class LearnerRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordService.class);

    private final URI learnerRecordSummariesUrl;
    private final URI learnerRecordEventsUrl;
    private final URI bookingUri;
    private final HttpService httpService;

    public LearnerRecordService(HttpService httpService,
                                @Value("${learnerRecord.summariesUrl}") URI learnerRecordSummariesUrl,
                                @Value("${learnerRecord.eventsUrl}") URI learnerRecordEventsUrl,
                                @Value("${learnerRecord.bookingUri}") URI bookingUri) {
        this.httpService = httpService;
        this.learnerRecordSummariesUrl = learnerRecordSummariesUrl;
        this.learnerRecordEventsUrl = learnerRecordEventsUrl;
        this.bookingUri = bookingUri;
    }

    @PreAuthorize("hasAnyAuthority('ORGANISATION_REPORTER', 'PROFESSION_REPORTER', 'CSHR_REPORTER')")
    public List<LearnerRecordSummary> listRecords() {
        LOGGER.debug("Listing records");
        return httpService.getList(learnerRecordSummariesUrl, LearnerRecordSummary.class);
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public List<LearnerRecordEvents> listEvents() {
        LOGGER.debug("Listing events");

        return httpService.getList(learnerRecordEventsUrl, LearnerRecordEvents.class);
    }

    public List<Booking> getBookings() {
        return httpService.getList(bookingUri, Booking.class);
    }
}
