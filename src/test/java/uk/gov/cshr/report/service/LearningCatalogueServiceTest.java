package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LearningCatalogueServiceTest {
    private URI eventUri;
    private URI moduleUri;
    private HttpService httpService;
    private LearningCatalogueService learningCatalogueService;
    private UriBuilderFactory uriBuilderFactory = mock(UriBuilderFactory.class);
    String modulesForCourseIdsUrl;

    @BeforeEach
    public void setUp() throws Exception {
        eventUri = new URI("http://example.org");
        httpService = mock(HttpService.class);
        learningCatalogueService = new LearningCatalogueService(httpService, uriBuilderFactory, eventUri, moduleUri, modulesForCourseIdsUrl);
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
