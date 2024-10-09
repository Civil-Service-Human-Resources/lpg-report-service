package uk.gov.cshr.report.client.identity.oauth;

import uk.gov.cshr.report.domain.identity.OAuthToken;

public interface IOAuthClient {
    OAuthToken getAccessToken();
}
