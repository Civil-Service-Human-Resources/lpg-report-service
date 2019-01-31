package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.gov.cshr.report.dto.catalogue.Event;
import uk.gov.cshr.report.dto.catalogue.Module;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LearningCatalogueServiceTest {
    private URI eventUri;
    private URI moduleUri;
    private String moduleByProfessionUri;
    private HttpService httpService;
    private LearningCatalogueService learningCatalogueService;

    @Before
    public void setUp() throws Exception {
        eventUri = new URI("http://event");
        moduleUri = new URI("http://module");
        moduleByProfessionUri = "http://module/%s";

        httpService = mock(HttpService.class);
        learningCatalogueService = new LearningCatalogueService(httpService, eventUri, moduleUri, moduleByProfessionUri);
    }

    @Test
    public void shouldReturnMapOfEvents() {
        Map<String, Event> eventMap = ImmutableMap.of("event-id", new Event());
        when(httpService.getMap(eventUri, Event.class)).thenReturn(eventMap);
        assertEquals(eventMap, learningCatalogueService.getEventMap());
    }

    @Test
    public void shouldReturnMapOfModules() {
        Map<String, Module> moduleMap = ImmutableMap.of("module-id", new Module());
        when(httpService.getMap(moduleUri, Module.class)).thenReturn(moduleMap);
        assertEquals(moduleMap, learningCatalogueService.getModuleMap());
    }

    @Test
    public void shouldReturnMapOfModulesForProfession() {
        int professionId = 2;
        URI uri = URI.create(String.format(moduleByProfessionUri, professionId));
        Map<String, Module> moduleMap = ImmutableMap.of("module-id", new Module());

        when(httpService.getMap(uri, Module.class)).thenReturn(moduleMap);
        assertEquals(moduleMap, learningCatalogueService.getModuleMap(professionId));
    }
}