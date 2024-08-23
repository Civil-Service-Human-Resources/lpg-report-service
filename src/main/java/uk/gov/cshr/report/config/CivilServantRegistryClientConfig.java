package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class CivilServantRegistryClientConfig {
    @Value("${registryService.serviceUrl}")
    private String civilServantRegistryServiceBaseUrl;

    private final OAuthClientFactory oAuthClientFactory;

    public CivilServantRegistryClientConfig(OAuthClientFactory oAuthClientFactory) {
        this.oAuthClientFactory = oAuthClientFactory;
    }

    @Bean(name = "civilServantRegistryHttpClient")
    IHttpClient civilServantRegistryClient(){
        return oAuthClientFactory.build(civilServantRegistryServiceBaseUrl);
    }
}
