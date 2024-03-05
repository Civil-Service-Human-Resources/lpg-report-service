package uk.gov.cshr.report.client.learningCatalogue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;

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

    public LearningCatalogueClient(@Qualifier("learningCatalogueHttpClient") IHttpClient httpClient){
        this.httpClient = httpClient;
    }
    @Override
    public Map<String, Event> getEvents() {
        RequestEntity<Void> request = RequestEntity.get(learningCatalogueEventsUrl).build();
        return httpClient.executeRequest(request, Map.class);
    }

    @Override
    public Map<String, Module> getModulesForCourseIds(String courseIds) {
        String url = String.format("%s?courseIds=%s", modulesForCourseIdsUrl, courseIds);
        RequestEntity<Void> request = RequestEntity.get(url).build();
        return httpClient.executeRequest(request, Map.class);
    }

    @Override
    public Map<String, Module> getReportingModules() {
        RequestEntity<Void> request = RequestEntity.get(learningCatalogueReportingModulesUrl).build();
        return httpClient.executeRequest(request, Map.class);
    }
}
