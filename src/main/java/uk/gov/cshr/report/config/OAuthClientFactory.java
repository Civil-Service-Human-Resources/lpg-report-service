package uk.gov.cshr.report.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.client.HttpClient;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.service.auth.RestTemplateOAuthInterceptor;

@Service
public class OAuthClientFactory {

    private final RestTemplateOAuthInterceptor restTemplateOAuthInterceptor;
    private final RestTemplateBuilder restTemplateBuilder;

    public OAuthClientFactory(RestTemplateOAuthInterceptor restTemplateOAuthInterceptor, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateOAuthInterceptor = restTemplateOAuthInterceptor;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    IHttpClient build(String baseUrl){
        RestTemplate restTemplate = restTemplateBuilder
                .rootUri(baseUrl)
                .additionalInterceptors(restTemplateOAuthInterceptor)
                .build();

        return new HttpClient(restTemplate);
    }
}
