package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.cshr.report.client.civilServantRegistry.ICivilServantRegistryClient;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CivilServantRegistryServiceTest {
    private URI civilServantUri;
    private HttpService httpService;
    private CivilServantRegistryService civilServantRegistryService;
    private UriBuilderFactory uriBuilderFactory = mock(UriBuilderFactory.class);
    private String civilServantsForLearnerIdsUrl;

    private ICivilServantRegistryClient civilServantRegistryClient;

    @BeforeEach
    public void setUp() throws Exception {
        civilServantUri = new URI("http://example.org");
        civilServantsForLearnerIdsUrl = "http://localhost/report/civil-servants-for-uids";
        httpService = mock(HttpService.class);
        civilServantRegistryClient = mock(ICivilServantRegistryClient.class);
        civilServantRegistryService = new CivilServantRegistryService(httpService, uriBuilderFactory, civilServantUri, civilServantsForLearnerIdsUrl, civilServantRegistryClient);
    }

    @Test
    public void testCivilServantRegistryServiceGetCivilServantMapReturnsCivilServantMapReturnedByClient() {
        CivilServant cs1 = new CivilServant();
        cs1.setId("abc");
        cs1.setGrade("G7");
        cs1.setName("Servant Name");
        cs1.setEmail("test@email.com");

        CivilServant cs2 = new CivilServant();
        cs2.setId("abc-2");
        cs2.setGrade("SEO");
        cs2.setName("Servant Name2");
        cs2.setEmail("test2@email.com");

        Map<String, CivilServant> fakeMap = new HashedMap<>();
        fakeMap.put("abc", cs1);
        fakeMap.put("abc-2", cs2);

        when(civilServantRegistryClient.getCivilServants()).thenReturn(fakeMap);

        Map<String, CivilServant> civilServantMapFromService = civilServantRegistryService.getCivilServantMap();

        assertEquals(2, civilServantMapFromService.size());
        assertEquals("abc", civilServantMapFromService.get("abc").getId());
        assertEquals("abc-2", civilServantMapFromService.get("abc-2").getId());
    }
}
