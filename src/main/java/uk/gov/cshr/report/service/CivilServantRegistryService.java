package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.registry.CivilServant;

import java.net.URI;
import java.util.Map;

@Service
public class CivilServantRegistryService {

    private final HttpService httpService;
    private final URI civilServantUri;

    public CivilServantRegistryService(HttpService httpService,
                                       @Value("${registryService.civilServantsUrl}") URI civilServantUri) {
        this.httpService = httpService;
        this.civilServantUri = civilServantUri;
    }

    public Map<String, CivilServant> getCivilServantMap() {
        return httpService.getMap(civilServantUri, CivilServant.class);
    }
}
