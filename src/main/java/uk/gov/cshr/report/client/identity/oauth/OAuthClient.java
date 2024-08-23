package uk.gov.cshr.report.client.identity.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.identity.OAuthToken;

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
    public OAuthToken getAccessToken() {

        RequestEntity<Void> request = RequestEntity
                .post(oauthTokenUrl + "?grant_type=client_credentials").build();
        return client.executeRequest(request, OAuthToken.class);
    }
}
