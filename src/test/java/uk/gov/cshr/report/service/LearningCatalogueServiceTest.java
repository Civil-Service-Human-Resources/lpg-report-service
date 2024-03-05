package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.cshr.report.client.learningCatalogue.ILearningCatalogueClient;
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

    ILearningCatalogueClient learningCatalogueClient;

    @BeforeEach
    public void setUp() throws Exception {
        eventUri = new URI("http://example.org");
        httpService = mock(HttpService.class);
        this.learningCatalogueClient = mock(ILearningCatalogueClient.class);
        learningCatalogueService = new LearningCatalogueService(httpService, uriBuilderFactory, eventUri, moduleUri, modulesForCourseIdsUrl, learningCatalogueClient);
    }

    @Test
    public void testLearningCatalogueGetEventMapShouldReturnEventMapReturnedFromLearningCatalogueClient() {

        Event event1 = new Event();
        event1.setId("123");

        Event event2 = new Event();
        event2.setId("456");

        Map<String, Event> fakeEventMap = new HashedMap<>();
        fakeEventMap.put("123", event1);
        fakeEventMap.put("456", event2);

        when(learningCatalogueClient.getEvents()).thenReturn(fakeEventMap);

        Map<String, Event> eventMapFromLearningCatalogueService = learningCatalogueService.getEventMap();

        assertEquals(2, eventMapFromLearningCatalogueService.size());
        assertEquals("123", eventMapFromLearningCatalogueService.get("123").getId());
        assertEquals("456", eventMapFromLearningCatalogueService.get("456").getId());
    }

    @Test
    public void shouldReturnMapOfModules() {
        Map<String, Module> moduleMap = ImmutableMap.of("module-id", new Module());
        when(httpService.getMap(moduleUri, Module.class)).thenReturn(moduleMap);
        assertEquals(moduleMap, learningCatalogueService.getModuleMap());
    }

}
