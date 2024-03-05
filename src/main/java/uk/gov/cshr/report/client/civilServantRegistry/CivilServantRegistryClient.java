package uk.gov.cshr.report.client.civilServantRegistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.registry.CivilServant;

import java.util.Map;

@Slf4j
@Component
public class CivilServantRegistryClient implements ICivilServantRegistryClient{

    @Value("${registryService.civilServantsUrl}")
    private String civilServantsUrl;

    private IHttpClient httpClient;

    public CivilServantRegistryClient(@Qualifier("civilServantRegistryHttpClient") IHttpClient httpClient){
        this.httpClient = httpClient;
    }

    @Override
    public Map<String, CivilServant> getCivilServants() {
        RequestEntity<Void> request = RequestEntity.get(civilServantsUrl).build();
        return httpClient.executeRequest(request, Map.class);
    }
}
