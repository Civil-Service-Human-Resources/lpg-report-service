package uk.gov.cshr.report.client.civilServantRegistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.domain.registry.GetCivilServantsForUidsParams;
import uk.gov.cshr.report.service.ParameterizedTypeReferenceFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.cshr.report.service.util.HttpUtils.batchList;

@Slf4j
@Component
public class CivilServantRegistryClient implements ICivilServantRegistryClient{

    @Value("${registryService.civilServantsUrl}")
    private String civilServantsUrl;

    @Value("${registryService.civilServantsForUidsUrl}")
    private String civilServantsForUidsUrl;

    @Value("${registryService.maxUidsSize}")
    private Integer maxUidsSize;

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
        return httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(CivilServant.class));
    }

    @Override
    public Map<String, CivilServant> getCivilServantMapForLearnerIds(Collection<String> learnerUids, Integer organisationId) {
        Map<String, CivilServant> map = new HashMap<>();
        batchList(maxUidsSize, learnerUids).forEach(batch -> {
            GetCivilServantsForUidsParams params = new GetCivilServantsForUidsParams(learnerUids, organisationId);
            RequestEntity<GetCivilServantsForUidsParams> request = RequestEntity.post(civilServantsForUidsUrl).body(params);
            Map<String, CivilServant> response = httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(CivilServant.class));
            if (response != null) {
                map.putAll(response);
            }
        });
        return map;
    }
}
