package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.civilServantRegistry.ICivilServantRegistryClient;
import uk.gov.cshr.report.domain.registry.CivilServant;

import java.util.List;
import java.util.Map;

@Service
public class CivilServantRegistryService {

    private final ICivilServantRegistryClient civilServantRegistryClient;

    public CivilServantRegistryService(ICivilServantRegistryClient civilServantRegistryClient) {
        this.civilServantRegistryClient = civilServantRegistryClient;
    }

    public Map<String, CivilServant> getCivilServantMap() {
        return civilServantRegistryClient.getCivilServants();
    }

    public Map<String, CivilServant> getCivilServantMapForLearnerIds(List<String> uids) {
        return civilServantRegistryClient.getCivilServantMapForLearnerIds(uids);
    }
}
