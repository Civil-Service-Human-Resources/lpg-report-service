package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class IdentitiesClientConfig {
    @Value("${oauth.serviceUrl}")
    private String identityServiceBaseUrl;

    private final OAuthClientFactory oAuthClientFactory;

    public IdentitiesClientConfig(OAuthClientFactory oAuthClientFactory) {
        this.oAuthClientFactory = oAuthClientFactory;
    }

    @Bean(name = "identitiesHttpClient")
    IHttpClient identitiesHttpClient(){
        return oAuthClientFactory.build(identityServiceBaseUrl);
    }
}
