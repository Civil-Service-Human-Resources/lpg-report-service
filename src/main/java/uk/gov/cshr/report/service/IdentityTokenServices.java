package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.stereotype.Service;

@Service
public class IdentityTokenServices extends RemoteTokenServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityTokenServices.class);

    @Autowired
    public IdentityTokenServices(@Value("${oauth.clientId}") String clientId,
                                 @Value("${oauth.clientSecret}") String clientSecret,
                                 @Value("${oauth.checkTokenEndpointUrl}") String checkTokenEndpointUrl) {
        setClientId(clientId);
        setClientSecret(clientSecret);
        setCheckTokenEndpointUrl(checkTokenEndpointUrl);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        LOGGER.debug("Loading authentication for access token {}", accessToken);
        return super.loadAuthentication(accessToken);
    }
}
