package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public CivilServantRegistryService(HttpService httpService,
                                       UriBuilderFactory uriBuilderFactory,
                                       @Value("${registryService.civilServantsUrl}") URI civilServantUri,
                                       @Value("${learnerRecord.civilServantsForUidsUrl}") String civilServantsForUidsUrl) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.civilServantUri = civilServantUri;
        this.civilServantsForUidsUrl = civilServantsForUidsUrl;
    }

    public Map<String, CivilServant> getCivilServantMap() {
        return httpService.getMap(civilServantUri, CivilServant.class);
    }

    public Map<String, CivilServant> getCivilServantMapForLearnerIds(String learnerIds) {
        URI uri = uriBuilderFactory.builder(civilServantsForUidsUrl)
                .queryParam("learnerIds", learnerIds)
                .build(new HashMap<>());
        return httpService.getMap(uri, CivilServant.class);
    }
}
