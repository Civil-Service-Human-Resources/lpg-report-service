package uk.gov.cshr.report.client.identity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.identity.Identity;

import java.util.Map;

@Slf4j
@Component
public class IdentitiesClient implements IIdentitiesClient{
    private IHttpClient httpClient;

    @Value("${oauth.identitiesListUrl}")
    private String identityIdentitiesListEndpointUrl;

    @Value("${oauth.identitiesMapForLearnersIdsUrl}")
    private String identitiesMapForLearnerUidsUrl;

    public IdentitiesClient(@Qualifier("identitiesHttpClient") IHttpClient httpClient){
        this.httpClient = httpClient;
    }
    @Override
    public Map<String, Identity> getIdentities() {
        RequestEntity<Void> request = RequestEntity.get(identityIdentitiesListEndpointUrl).build();
        return httpClient.executeRequest(request, Map.class);
    }

    @Override
    public Map<String, Identity> getIdentitiesFromUids(String commaSeparatedIdentityUids){
        String url = String.format("%s?uids=%s", identitiesMapForLearnerUidsUrl, commaSeparatedIdentityUids);
        RequestEntity<Void> request = RequestEntity.get(url).build();
        return httpClient.executeRequest(request, Map.class);
    }
}
