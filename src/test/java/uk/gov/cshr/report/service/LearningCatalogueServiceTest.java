package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LearningCatalogueServiceTest {
    private URI eventUri;
    private URI moduleUri;
    private URI mandatoryCoursesUri;
    private HttpService httpService;
    private LearningCatalogueService learningCatalogueService;

    @Before
    public void setUp() throws Exception {
        eventUri = new URI("http://example.org");
        httpService = mock(HttpService.class);
        learningCatalogueService = new LearningCatalogueService(httpService, eventUri, moduleUri, mandatoryCoursesUri);
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

}