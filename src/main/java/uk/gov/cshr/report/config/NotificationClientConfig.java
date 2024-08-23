package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class NotificationClientConfig {
    @Value("${notification.serviceUrl}")
    private String notificationServiceUrl;

    private final OAuthClientFactory oAuthClientFactory;

    public NotificationClientConfig(OAuthClientFactory oAuthClientFactory) {
        this.oAuthClientFactory = oAuthClientFactory;
    }

    @Bean(name = "notificationHttpClient")
    IHttpClient notificationClient(){
        return oAuthClientFactory.build(notificationServiceUrl);
    }


}
