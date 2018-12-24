package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;

import java.net.URI;
import java.util.Map;

@Service
public class LearningCatalogueService {

    private final HttpService httpService;

    private final URI eventUri;
    private final URI moduleUri;

    public LearningCatalogueService(HttpService httpService,
                                    @Value("${learningCatalogue.eventsUrl}") URI eventUri,
                                    @Value("${learningCatalogue.modulesUrl}") URI moduleUri
                                    ) {
        this.httpService = httpService;
        this.eventUri = eventUri;
        this.moduleUri = moduleUri;
    }

    public Map<String, Event> getEventMap() {
        return httpService.getMap(eventUri, Event.class);
    }

    public Map<String, Module> getModuleMap() {
        return httpService.getMap(moduleUri, Module.class);
    }

}
