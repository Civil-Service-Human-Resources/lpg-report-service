package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.client.HttpClient;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class NotificationClientConfig {
    @Value("${notification.serviceUrl}")
    private String notificationServiceUrl;

    @Bean(name = "notificationHttpClient")
    IHttpClient notificationClient(RestTemplateBuilder restTemplateBuilder){
        RestTemplate restTemplate = restTemplateBuilder
                .rootUri(notificationServiceUrl)
                .build();

        return new HttpClient(restTemplate);
    }


}
