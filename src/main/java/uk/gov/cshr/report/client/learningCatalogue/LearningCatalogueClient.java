package uk.gov.cshr.report.client.learningCatalogue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.service.ParameterizedTypeReferenceFactory;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
public class LearningCatalogueClient implements ILearningCatalogueClient{
    private IHttpClient httpClient;

    @Value("${learningCatalogue.eventsUrl}")
    private String learningCatalogueEventsUrl;

    @Value("${learningCatalogue.modulesForCourseIdsUrl}")
    private String modulesForCourseIdsUrl;

    @Value("${learningCatalogue.modulesUrl}")
    private String learningCatalogueReportingModulesUrl;

    private ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory;

    public LearningCatalogueClient(@Qualifier("learningCatalogueHttpClient") IHttpClient httpClient, ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory){
        this.httpClient = httpClient;
        this.parameterizedTypeReferenceFactory = parameterizedTypeReferenceFactory;

    }
    @Override
    public Map<String, Event> getEvents() {
        RequestEntity<Void> request = RequestEntity.get(learningCatalogueEventsUrl).build();
        return httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(Event.class));
    }

    @Override
    public Map<String, Module> getModulesForCourseIds(String courseIds) {
        String url = String.format("%s?courseIds=%s", modulesForCourseIdsUrl, courseIds);
        RequestEntity<Void> request = RequestEntity.get(url).build();
        return httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(Module.class));
    }

    @Override
    public Map<String, Module> getReportingModules() {
        RequestEntity<Void> request = RequestEntity.get(learningCatalogueReportingModulesUrl).build();
        return httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(Module.class));
    }
}
