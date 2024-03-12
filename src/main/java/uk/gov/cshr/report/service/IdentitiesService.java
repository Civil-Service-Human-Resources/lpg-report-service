package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.identity.IIdentitiesClient;
import uk.gov.cshr.report.domain.identity.Identity;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IdentitiesService {
    private IIdentitiesClient identitiesClient;

    public IdentitiesService(IIdentitiesClient identitiesClient){
        this.identitiesClient = identitiesClient;
    }

    public Map<String, Identity> getIdentities(){
        return identitiesClient.getIdentities();
    }

    public Map<String, Identity> getIdentitiesFromUids(List<String> identityUids){
        return identitiesClient.getIdentitiesFromUids(identityUids);
    }
}
