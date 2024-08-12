package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.client.HttpClient;
import uk.gov.cshr.report.client.IHttpClient;

@Configuration
public class OAuthClientConfig {
    @Value("${oauth.serviceUrl}")
    private String oAuthServiceUrl;

    @Bean(name = "oAuthClient")
    IHttpClient oAuthClient(RestTemplateBuilder restTemplateBuilder){
        RestTemplate restTemplate = restTemplateBuilder
                .rootUri(oAuthServiceUrl)
                .build();

        return new HttpClient(restTemplate);
    }


}
