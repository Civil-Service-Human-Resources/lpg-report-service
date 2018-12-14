package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.gov.cshr.report.domain.registry.CivilServant;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CivilServantRegistryServiceTest {
    private URI civilServantUri;
    private HttpService httpService;
    private CivilServantRegistryService civilServantRegistryService;

    @Before
    public void setUp() throws Exception {
        civilServantUri = new URI("http://example.org");
        httpService = mock(HttpService.class);
        civilServantRegistryService = new CivilServantRegistryService(httpService, civilServantUri);
    }

    @Test
    public void shouldReturnMapOfCivilServantsByUid() {

        Map<String, CivilServant> civilServantMap = ImmutableMap.of("civil-servant-uid", new CivilServant());

        when(httpService.getMap(civilServantUri, String.class, CivilServant.class)).thenReturn(civilServantMap);

        assertEquals(civilServantMap, civilServantRegistryService.getCivilServantMap());
    }
}