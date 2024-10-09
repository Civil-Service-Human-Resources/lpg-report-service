package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class LearningCatalogueClientConfig {
    @Value("${learningCatalogue.serviceUrl}")
    private String learningCatalogueServiceBaseUrl;

    private final OAuthClientFactory oAuthClientFactory;

    public LearningCatalogueClientConfig(OAuthClientFactory oAuthClientFactory) {
        this.oAuthClientFactory = oAuthClientFactory;
    }

    @Bean(name = "learningCatalogueHttpClient")
    IHttpClient learnerRecordClient(){
        return oAuthClientFactory.build(learningCatalogueServiceBaseUrl);
    }
}
