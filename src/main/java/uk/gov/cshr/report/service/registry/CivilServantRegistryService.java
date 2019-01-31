package uk.gov.cshr.report.service.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.dto.registry.CivilServant;
import uk.gov.cshr.report.exception.ProfessionNotSetException;
import uk.gov.cshr.report.service.HttpService;
import uk.gov.cshr.report.service.registry.domain.CivilServantResource;

import java.net.URI;
import java.util.Map;

@Service
public class CivilServantRegistryService {

    private final HttpService httpService;
    private final URI civilServantsUri;
    private final URI civilServantProfileUri;

    public CivilServantRegistryService(HttpService httpService,
                                       @Value("${registryService.civilServantsUrl}") URI civilServantsUri,
                                       @Value("${registryService.civilServantProfileUri}") URI civilServantProfileUri) {
        this.httpService = httpService;
        this.civilServantsUri = civilServantsUri;
        this.civilServantProfileUri = civilServantProfileUri;
    }

    public Map<String, CivilServant> getCivilServantMap() {
        return httpService.getMap(civilServantsUri, CivilServant.class);
    }

    public boolean userHasProfession(long professionId) {
        CivilServantResource civilServant = httpService.get(civilServantProfileUri, CivilServantResource.class);

        if (null == civilServant.getProfession()) {
            throw new ProfessionNotSetException();
        }

        return civilServant.getProfession().getId() == professionId;
    }
}
