package uk.gov.cshr.report.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {
    String getAccessToken() {
        OAuth2AuthenticationDetailsWrapper oAuth2AuthenticationDetailsWrapper = new OAuth2AuthenticationDetailsWrapper();
        return oAuth2AuthenticationDetailsWrapper.getTokenValue();
    }

    String getAccessToken(OAuth2AuthenticationDetailsWrapper oAuth2AuthenticationDetailsWrapper){
        return oAuth2AuthenticationDetailsWrapper.getTokenValue();
    }
}
