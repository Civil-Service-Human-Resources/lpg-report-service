package uk.gov.cshr.report.client.identity;

import uk.gov.cshr.report.domain.identity.Identity;

import java.util.List;
import java.util.Map;

public interface IIdentitiesClient {
    public Map<String, Identity> getIdentities();

    public Map<String, Identity> getIdentitiesFromUids(List<String> identityUids);
}
