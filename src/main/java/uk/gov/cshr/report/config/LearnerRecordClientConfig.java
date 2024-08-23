package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class LearnerRecordClientConfig {
    @Value("${learnerRecord.serviceUrl}")
    private String learnerRecordServiceUrl;

    private final OAuthClientFactory oAuthClientFactory;

    public LearnerRecordClientConfig(OAuthClientFactory oAuthClientFactory) {
        this.oAuthClientFactory = oAuthClientFactory;
    }

    @Bean(name = "learnerRecordHttpClient")
    IHttpClient learnerRecordClient(){
        return oAuthClientFactory.build(learnerRecordServiceUrl);
    }


}
