package uk.gov.cshr.report.service;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.domain.LearnerRecordEvents;
import uk.gov.cshr.report.domain.LearnerRecordSummary;
import uk.gov.cshr.report.domain.learnerrecord.Booking;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;

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
