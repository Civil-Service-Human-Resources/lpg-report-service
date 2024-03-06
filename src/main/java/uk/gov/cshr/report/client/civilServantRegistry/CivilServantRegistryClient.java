package uk.gov.cshr.report.client.civilServantRegistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.service.ParameterizedTypeReferenceFactory;

import java.util.Map;

@Slf4j
@Component
public class CivilServantRegistryClient implements ICivilServantRegistryClient{

    @Value("${registryService.civilServantsUrl}")
    private String civilServantsUrl;

    @Value("${registryService.civilServantsForUidsUrl}")
    private String civilServantsForUidsUrl;

    private IHttpClient httpClient;
    ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory;

    public CivilServantRegistryClient(
            @Qualifier("civilServantRegistryHttpClient") IHttpClient httpClient,
            ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory){
        this.httpClient = httpClient;
        this.parameterizedTypeReferenceFactory = parameterizedTypeReferenceFactory;
    }

    @Override
    public Map<String, CivilServant> getCivilServants() {
        RequestEntity<Void> request = RequestEntity.get(civilServantsUrl).build();
        return httpClient.executeRequest(request, parameterizedTypeReferenceFactory.createMapReference(CivilServant.class));
    }

    @Override
    public Map<String, CivilServant> getCivilServantMapForLearnerIds(String commaSeparatedLearnerUids) {
        String url = String.format("%s?uids=%s", civilServantsForUidsUrl, commaSeparatedLearnerUids);
        RequestEntity<Void> request = RequestEntity.get(url).build();

        return httpClient.executeRequest(request, parameterizedTypeReferenceFactory.createMapReference(CivilServant.class));
    }
}
