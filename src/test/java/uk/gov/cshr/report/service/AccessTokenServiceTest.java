package uk.gov.cshr.report.service;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.identity.OAuthToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccessTokenServiceTest {
    @Spy
    AccessTokenService accessTokenService = new AccessTokenService(mock(IHttpClient.class));

    @Test
    public void testGetAccessTokenShouldReturnAccessTokenRetrievedFromIdentityService(){
        String tokenValue = "token-value";

        OAuthToken fakeOAuthToken = new OAuthToken();
        fakeOAuthToken.setAccessToken(tokenValue);
        when(accessTokenService.getOAuthToken()).thenReturn(fakeOAuthToken);

        assertEquals(tokenValue, accessTokenService.getAccessToken());
    }
}