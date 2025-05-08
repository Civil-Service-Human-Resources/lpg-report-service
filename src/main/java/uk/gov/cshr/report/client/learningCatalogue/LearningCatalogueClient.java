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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.cshr.report.service.util.HttpUtils.batchList;

@Slf4j
@Component
public class LearningCatalogueClient implements ILearningCatalogueClient{
    private IHttpClient httpClient;

    @Value("${learningCatalogue.eventsUrl}")
    private String learningCatalogueEventsUrl;

    @Value("${learningCatalogue.modulesForCourseIdsUrl}")
    private String modulesForCourseIdsUrl;

    @Value("${learningCatalogue.modulesForCourseIdsBatchSize}")
    private Integer modulesForCourseIdsBatchSize;

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
    public Map<String, Module> getModulesForCourseIds(List<String> courseIds) {
        Map<String, Module> moduleMap = new HashMap<>();
        batchList(modulesForCourseIdsBatchSize, courseIds).forEach(batch -> {
            String url = String.format("%s?courseIds=%s", modulesForCourseIdsUrl, String.join(",", batch));
            RequestEntity<Void> request = RequestEntity.get(url).build();
            Map<String, Module> response = httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(Module.class));
            if (response != null) {
                moduleMap.putAll(response);
            }
        });
        return moduleMap;
    }

    @Override
    public Map<String, Module> getReportingModules() {
        RequestEntity<Void> request = RequestEntity.get(learningCatalogueReportingModulesUrl).build();
        return httpClient.executeMapRequest(request, parameterizedTypeReferenceFactory.createMapReference(Module.class));
    }
}
