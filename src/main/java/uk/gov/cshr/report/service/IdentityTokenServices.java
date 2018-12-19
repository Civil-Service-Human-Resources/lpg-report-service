package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.config.OAuthProperties;

@Service
public class IdentityTokenServices extends RemoteTokenServices {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityTokenServices.class);

    @Autowired
    public IdentityTokenServices(OAuthProperties properties) {
        setClientId(properties.getClientId());
        setClientSecret(properties.getClientSecret());
        setCheckTokenEndpointUrl(properties.getCheckTokenUrl());
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) {
        LOG.debug("Loading authentication for access token {}", accessToken);
        return super.loadAuthentication(accessToken);
    }
}
