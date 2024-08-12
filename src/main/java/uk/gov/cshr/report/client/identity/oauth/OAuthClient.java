package uk.gov.cshr.report.client.identity.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.identity.OAuthToken;
import uk.gov.cshr.report.service.ParameterizedTypeReferenceFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OAuthClient implements IOAuthClient {
    private final IHttpClient client;

    public OAuthClient(@Qualifier("oAuthClient") IHttpClient client) {
        this.client = client;
    }

    @Value("${oauth.tokenUrl}")
    private String oauthTokenUrl;

    @Override
    public String getAccessToken(String basicAuthClientId, String basicAuthClientSecret) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(basicAuthClientId, basicAuthClientSecret);

        RequestEntity<Void> request = RequestEntity
                .post(oauthTokenUrl + "?grant_type=client_credentials")
                .headers(headers).build();

        OAuthToken response = client.executeRequest(request, OAuthToken.class);

        return response.getAccessToken();
    }
}
