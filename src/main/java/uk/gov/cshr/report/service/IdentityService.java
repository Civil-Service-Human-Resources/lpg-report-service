package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdentityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordService.class);

    private final HttpService httpService;
    private final UriBuilderFactory uriBuilderFactory;
    private final URI identitiesListUrl;
    private final String identitiesMapForLearnersIdsUrl;

    public IdentityService(HttpService httpService,
                           UriBuilderFactory uriBuilderFactory,
                           @Value("${oauth.identitiesListUrl}") URI identitiesListUrl,
                           @Value("${oauth.identitiesMapForLearnersIdsUrl}") String identitiesMapForLearnersIdsUrl
                           ) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.identitiesListUrl = identitiesListUrl;
        this.identitiesMapForLearnersIdsUrl = identitiesMapForLearnersIdsUrl;
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public Map<String, Identity> getIdentitiesMap() {
        LOGGER.debug("Listing identities");
        return httpService.getMap(identitiesListUrl, Identity.class);
    }

    public Map<String, Identity> getIdentitiesMapForLearners(List<String> learnerIds) {
        LOGGER.debug("Listing identities for learnerIds: " + learnerIds);
        URI uri = uriBuilderFactory.builder(identitiesMapForLearnersIdsUrl)
                .queryParam("uids", learnerIds)
                .build(new HashMap<>());
        return httpService.getMap(uri, Identity.class);
    }
}
