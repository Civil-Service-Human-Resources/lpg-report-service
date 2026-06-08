package uk.gov.cshr.report.client.civilServantRegistry;

import uk.gov.cshr.report.domain.registry.CivilServant;

import java.util.Collection;
import java.util.Map;

public interface ICivilServantRegistryClient {
    Map<String, CivilServant> getCivilServants();

    Map<String, CivilServant> getCivilServantMapForLearnerIds(Collection<String> learnerUids, Integer organisationId);
}
