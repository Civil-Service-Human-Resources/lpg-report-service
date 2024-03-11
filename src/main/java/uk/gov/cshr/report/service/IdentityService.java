package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.identity.IIdentityClient;
import uk.gov.cshr.report.domain.identity.OAuthToken;

import java.time.LocalDateTime;

@Service
@Slf4j
public class IdentityService {
    private final IIdentityClient identityClient;

    public IdentityService(IIdentityClient identityClient) {
        this.identityClient = identityClient;
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
