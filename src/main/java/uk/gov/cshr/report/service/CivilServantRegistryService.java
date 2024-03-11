package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.civilServantRegistry.CivilServantRegistryClient;
import uk.gov.cshr.report.client.civilServantRegistry.ICivilServantRegistryClient;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.util.HashMap;
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

    public Map<String, CivilServant> getCivilServantMapForLearnerIds(String uids) {
        return civilServantRegistryClient.getCivilServantMapForLearnerIds(uids);
    }
}
