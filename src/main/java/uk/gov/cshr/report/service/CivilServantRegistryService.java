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

    private final HttpService httpService;
    private final UriBuilderFactory uriBuilderFactory;
    private final URI civilServantUri;
    private final String civilServantsForUidsUrl;

    private final ICivilServantRegistryClient civilServantRegistryClient;

    public CivilServantRegistryService(HttpService httpService,
                                       UriBuilderFactory uriBuilderFactory,
                                       @Value("${registryService.civilServantsUrl}") URI civilServantUri,
                                       @Value("${registryService.civilServantsForUidsUrl}") String civilServantsForUidsUrl,
                                       ICivilServantRegistryClient civilServantRegistryClient) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.civilServantUri = civilServantUri;
        this.civilServantsForUidsUrl = civilServantsForUidsUrl;
        this.civilServantRegistryClient = civilServantRegistryClient;
    }

    public Map<String, CivilServant> getCivilServantMap() {
        return civilServantRegistryClient.getCivilServants();
    }

    public Map<String, CivilServant> getCivilServantMapForLearnerIds(String uids) {
        URI uri = uriBuilderFactory.builder(civilServantsForUidsUrl)
                .queryParam("uids", uids)
                .build(new HashMap<>());
        return httpService.getMap(uri, CivilServant.class);
    }
}
