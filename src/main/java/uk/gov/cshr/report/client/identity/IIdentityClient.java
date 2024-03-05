package uk.gov.cshr.report.client.identity;

import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.identity.OAuthToken;

import java.util.Map;

public interface IIdentityClient {

    OAuthToken getServiceToken();
}
