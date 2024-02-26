package uk.gov.cshr.report.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccessTokenServiceTest {

    @Test
    public void testGetAccessTokenShouldReturnAccessTokenRetrievedFromOAuth2AuthenticationDetails(){
        String tokenValue = "token-value";

        OAuth2AuthenticationDetailsWrapper wrapper = mock(OAuth2AuthenticationDetailsWrapper.class);
        when(wrapper.getTokenValue()).thenReturn(tokenValue);

        AccessTokenService accessTokenService = new AccessTokenService();
        assertEquals(tokenValue, accessTokenService.getAccessToken(wrapper));
    }
}