package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.identity.Identity;

import java.net.URI;
import java.util.Map;

@Service
public class IdentityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordService.class);

    private final HttpService httpService;
    private final URI identitiesListUrl;

    public IdentityService(HttpService httpService,
                           @Value("${oauth.identitiesListUrl}") URI identitiesListUrl
    ) {
        this.httpService = httpService;
        this.identitiesListUrl = identitiesListUrl;
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public Map<String, Identity> getIdentitiesMap() {
        LOGGER.debug("Listing identities");

        return httpService.getMap(identitiesListUrl, Identity.class);
    }
}
