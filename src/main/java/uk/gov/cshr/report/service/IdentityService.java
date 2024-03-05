package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.identity.IIdentitiesClient;
import uk.gov.cshr.report.client.identity.IdentityClient;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.identity.OAuthToken;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class IdentityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordService.class);

    private final HttpService httpService;
    private final UriBuilderFactory uriBuilderFactory;
    private final URI identitiesListUrl;
    private final String identitiesMapForLearnersIdsUrl;

    private final IdentityClient identityClient;

    public IdentityService(HttpService httpService,
                           UriBuilderFactory uriBuilderFactory,
                           @Value("${oauth.identitiesListUrl}") URI identitiesListUrl,
                           @Value("${oauth.identitiesMapForLearnersIdsUrl}") String identitiesMapForLearnersIdsUrl,
                           IdentityClient identityClient
                           ) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.identitiesListUrl = identitiesListUrl;
        this.identitiesMapForLearnersIdsUrl = identitiesMapForLearnersIdsUrl;
        this.identityClient = identityClient;
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public Map<String, Identity> getIdentitiesMap() {
        LOGGER.debug("Listing identities");
        return httpService.getMap(identitiesListUrl, Identity.class);
    }

    public Map<String, Identity> getIdentitiesMapForLearners(String learnerIds) {
        LOGGER.debug("Listing identities for learnerIds: " + learnerIds);
        URI uri = uriBuilderFactory.builder(identitiesMapForLearnersIdsUrl)
                .queryParam("uids", learnerIds)
                .build(new HashMap<>());
        return httpService.getMap(uri, Identity.class);
    }

    @Cacheable("service-token")
    public OAuthToken getCachedOAuthServiceToken() {
        OAuthToken oAuthToken = new OAuthToken();
        try {
            oAuthToken = identityClient.getServiceToken();
            oAuthToken.setExpiryDateTime(LocalDateTime.now().plusSeconds(oAuthToken.getExpiresIn()));
        } catch (Exception e) {
            log.error("Unable to retrieve service token from identity-service. " +
                    "Following response is received from identity-service: {}", e.getMessage());
        }
        return oAuthToken;
    }

    @CacheEvict(value = "service-token", allEntries = true)
    public void removeServiceTokenFromCache() {
        log.info("IdentityService.removeServiceTokenFromCache: service token is removed from the cache.");
    }
}
