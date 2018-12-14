package uk.gov.cshr.report.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
@PowerMockIgnore("javax.security.*")
public class AccessTokenServiceTest {

    private AccessTokenService accessTokenService = new AccessTokenService();

    @Test
    public void shouldReturnAccessToken() {
        String tokenValue = "token-value";

        mockStatic(SecurityContextHolder.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = mock(OAuth2AuthenticationDetails.class);
        when(authentication.getDetails()).thenReturn(oAuth2AuthenticationDetails);

        when(oAuth2AuthenticationDetails.getTokenValue()).thenReturn(tokenValue);

        assertEquals(tokenValue, accessTokenService.getAccessToken());
    }
}