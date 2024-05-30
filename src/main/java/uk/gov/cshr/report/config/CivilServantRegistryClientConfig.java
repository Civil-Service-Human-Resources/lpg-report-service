package uk.gov.cshr.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.client.HttpClient;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.service.auth.RestTemplateOAuthInterceptor;

@Configuration
public class CivilServantRegistryClientConfig {
    @Value("${registryService.serviceUrl}")
    private String civilServantRegistryServiceBaseUrl;

    private final RestTemplateOAuthInterceptor restTemplateOAuthInterceptor;

    public CivilServantRegistryClientConfig(RestTemplateOAuthInterceptor restTemplateOAuthInterceptor){
        this.restTemplateOAuthInterceptor = restTemplateOAuthInterceptor;
    }

    @Bean(name = "civilServantRegistryHttpClient")
    IHttpClient civilServantRegistryClient(RestTemplateBuilder restTemplateBuilder){
        RestTemplate restTemplate = restTemplateBuilder
                .rootUri(civilServantRegistryServiceBaseUrl)
                .additionalInterceptors(restTemplateOAuthInterceptor)
                .build();

        return new HttpClient(restTemplate);
    }
}
