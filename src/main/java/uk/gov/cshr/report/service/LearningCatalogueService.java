package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.dto.catalogue.Event;
import uk.gov.cshr.report.dto.catalogue.Module;

import java.net.URI;
import java.util.Map;

@Service
public class LearningCatalogueService {

    private final HttpService httpService;

    private final URI eventUri;
    private final URI moduleUri;
    private final String moduleByProfessionUri;

    public LearningCatalogueService(HttpService httpService,
                                    @Value("${learningCatalogue.eventsUrl}") URI eventUri,
                                    @Value("${learningCatalogue.modulesUrl}") URI moduleUri,
                                    @Value("${learningCatalogue.modulesByProfessionUrl}") String modulesByProfessionUri
                                    ) {
        this.httpService = httpService;
        this.eventUri = eventUri;
        this.moduleUri = moduleUri;
        this.moduleByProfessionUri = modulesByProfessionUri;
    }

    public Map<String, Event> getEventMap() {
        return httpService.getMap(eventUri, Event.class);
    }

    public Map<String, Module> getModuleMap() {
        return httpService.getMap(moduleUri, Module.class);
    }

    public Map<String, Module> getModuleMap(long professionId) {
        URI uri = URI.create(String.format(moduleByProfessionUri, professionId));
        return httpService.getMap(uri, Module.class);
    }
}
