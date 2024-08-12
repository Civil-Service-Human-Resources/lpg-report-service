package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.identity.oauth.IOAuthClient;

@Service
@Slf4j
public class OAuthService {
    @Value("${oauth.clientId}")
    private String basicAuthClientId;

    @Value("${oauth.clientSecret}")
    private String basicAuthClientSecret;

    private IOAuthClient oauthClient;

    public OAuthService(IOAuthClient oauthClient) {
        this.oauthClient = oauthClient;
    }

    public String getAccessToken(){
        return oauthClient.getAccessToken(basicAuthClientId, basicAuthClientSecret);
    }
}
