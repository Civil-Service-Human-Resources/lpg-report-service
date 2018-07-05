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

import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;

@Service
public class LearnerRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordService.class);

    private RestTemplate restTemplate;

    private URI learnerRecordSummariesUrl;

    private URI learnerRecordEventsUrl;

    @Autowired
    public LearnerRecordService(RestTemplate restTemplate,
                                @Value("${learnerRecord.summariesUrl}") URI learnerRecordSummariesUrl,
                                @Value("${learnerRecord.eventsUrl}") URI learnerRecordEventsUrl) {
        checkArgument(restTemplate != null);
        checkArgument(learnerRecordSummariesUrl != null);
        checkArgument(learnerRecordEventsUrl != null);
        this.restTemplate = restTemplate;
        this.learnerRecordSummariesUrl = learnerRecordSummariesUrl;
        this.learnerRecordEventsUrl = learnerRecordEventsUrl;
    }

    @PreAuthorize("hasAnyAuthority('ORGANISATION_REPORTER', 'PROFESSION_REPORTER', 'CSHR_REPORTER')")
    public List<LearnerRecordSummary> listRecords() {
        LOGGER.debug("Listing records");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + details.getTokenValue());

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, learnerRecordSummariesUrl);

        ResponseEntity<LearnerRecordSummary[]> response = restTemplate.exchange(requestEntity, LearnerRecordSummary[].class);
        if (response.hasBody()) {
            return Lists.newArrayList(response.getBody());
        }
        return emptyList();
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public List<LearnerRecordEvents> listEvents() {
        LOGGER.debug("Listing events");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + details.getTokenValue());

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, learnerRecordEventsUrl);

        ResponseEntity<LearnerRecordEvents[]> response = restTemplate.exchange(requestEntity, LearnerRecordEvents[].class);
        if (response.hasBody()) {
            return Lists.newArrayList(response.getBody());
        }
        return emptyList();
    }
}
