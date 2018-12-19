package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.gov.cshr.report.domain.catalogue.Event;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LearningCatalogueServiceTest {
    private URI eventUri;
    private HttpService httpService;
    private LearningCatalogueService learningCatalogueService;

    @Before
    public void setUp() throws Exception {
        eventUri = new URI("http://example.org");
        httpService = mock(HttpService.class);
        learningCatalogueService = new LearningCatalogueService(httpService, eventUri);
    }

    @Test
    public void shouldReturnMapOfEvents() {
        Map<String, Event> eventMap = ImmutableMap.of("event-id", new Event());
        when(httpService.getMap(eventUri, Event.class)).thenReturn(eventMap);
        assertEquals(eventMap, learningCatalogueService.getEventMap());
    }
}