package uk.gov.cshr.report.service.registry;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.service.HttpService;
import uk.gov.cshr.report.service.registry.CivilServantRegistryService;
import uk.gov.cshr.report.service.registry.domain.CivilServantResource;
import uk.gov.cshr.report.service.registry.domain.Profession;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CivilServantRegistryServiceTest {
    private URI civilServantsUri;
    private URI civilServantProfileUri;
    private HttpService httpService;
    private CivilServantRegistryService civilServantRegistryService;

    @Before
    public void setUp() throws Exception {
        civilServantsUri = new URI("http://example.org");
        civilServantProfileUri = new URI("http://localhost/me");
        httpService = mock(HttpService.class);
        civilServantRegistryService = new CivilServantRegistryService(httpService, civilServantsUri, civilServantProfileUri);
    }

    @Test
    public void shouldReturnMapOfCivilServantsByUid() {

        Map<String, CivilServant> civilServantMap = ImmutableMap.of("civil-servant-uid", new CivilServant());

        when(httpService.getMap(civilServantsUri, CivilServant.class)).thenReturn(civilServantMap);

        assertEquals(civilServantMap, civilServantRegistryService.getCivilServantMap());
    }

    @Test
    public void shouldReturnTrueIfUserProfessionIdMatchesProfessionId() {
        long professionId = 99;
        Profession profession = new Profession();
        profession.setId(professionId);

        CivilServantResource civilServantResource = new CivilServantResource();
        civilServantResource.setProfession(profession);

        when(httpService.get(civilServantProfileUri, CivilServantResource.class))
                .thenReturn(civilServantResource);

        assertTrue(civilServantRegistryService.userHasProfession(professionId));
    }

    @Test
    public void shouldReturnFalseIfUserProfessionIdDoesNotMatchProfessionId() {
        long professionId = 99;
        Profession profession = new Profession();
        profession.setId(100);

        CivilServantResource civilServantResource = new CivilServantResource();
        civilServantResource.setProfession(profession);

        when(httpService.get(civilServantProfileUri, CivilServantResource.class))
                .thenReturn(civilServantResource);

        assertFalse(civilServantRegistryService.userHasProfession(professionId));
    }
}