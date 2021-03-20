package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LearningCatalogueService {

    private final HttpService httpService;
    private final UriBuilderFactory uriBuilderFactory;
    private final URI eventUri;
    private final URI moduleUri;
    private final String modulesForCourseIdsUrl;

    public LearningCatalogueService(HttpService httpService,
                                    UriBuilderFactory uriBuilderFactory,
                                    @Value("${learningCatalogue.eventsUrl}") URI eventUri,
                                    @Value("${learningCatalogue.modulesUrl}") URI moduleUri,
                                    @Value("${learningCatalogue.modulesForCourseIdsUrl}") String modulesForCourseIdsUrl
                                    ) {
        this.httpService = httpService;
        this.uriBuilderFactory = uriBuilderFactory;
        this.eventUri = eventUri;
        this.moduleUri = moduleUri;
        this.modulesForCourseIdsUrl = modulesForCourseIdsUrl;
    }

    public Map<String, Event> getEventMap() {
        return httpService.getMap(eventUri, Event.class);
    }

    public Map<String, Module> getModuleMap() {
        return httpService.getMap(moduleUri, Module.class);
    }

    public Map<String, Module> getModuleMapForCourseIds(String courseIds) {
        URI uri = uriBuilderFactory.builder(modulesForCourseIdsUrl)
                .queryParam("courseIds", courseIds)
                .build(new HashMap<>());
        return httpService.getMap(uri, Module.class);
    }
}
