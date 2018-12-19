package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Event;

import java.net.URI;
import java.util.Map;

@Service
public class LearningCatalogueService {

    private final HttpService httpService;

    private final URI eventUri;

    public LearningCatalogueService(HttpService httpService,
                        @Value("${learningCatalogue.eventsUrl}") URI eventUri) {
        this.httpService = httpService;
        this.eventUri = eventUri;
    }

    public Map<String, Event> getEventMap() {
        return httpService.getMap(eventUri, Event.class);
    }
}
