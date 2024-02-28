package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.identity.OAuthToken;


@Service
public class AccessTokenService {
    @Qualifier("identityHttpClient")
    private final IHttpClient client;

    @Value("${oauth.tokenUrl}")
    private String tokenRetrievalUrl;


    public AccessTokenService(@Qualifier("identityHttpClient") IHttpClient client) {
        this.client = client;
    }

    String getAccessToken(){
        OAuthToken oAuthToken = getOAuthToken();
        if (oAuthToken == null) {
            throw new RuntimeException("Service token response was null");
        }
        return oAuthToken.getAccessToken();
    }

    OAuthToken getOAuthToken(){
        String url = String.format("%s?grant_type=client_credentials", tokenRetrievalUrl);
        RequestEntity<Void> request = RequestEntity.post(url).build();
        OAuthToken response = client.executeRequest(request, OAuthToken.class);
        return response;
    }
}
