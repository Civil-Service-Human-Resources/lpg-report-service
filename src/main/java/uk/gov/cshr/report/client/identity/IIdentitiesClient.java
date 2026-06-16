package uk.gov.cshr.report.client.identity;

import uk.gov.cshr.report.domain.identity.Identity;

import java.util.Collection;
import java.util.Map;

public interface IIdentitiesClient {

    Map<String, Identity> getIdentitiesFromUids(Collection<String> identityUids);
}
