package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.learningCatalogue.ILearningCatalogueClient;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;

import java.util.List;
import java.util.Map;

@Service
public class LearningCatalogueService {

    private final ILearningCatalogueClient learningCatalogueClient;

    public LearningCatalogueService(ILearningCatalogueClient learningCatalogueClient) {
        this.learningCatalogueClient = learningCatalogueClient;
    }

    public Map<String, Event> getEventMap() {
        return learningCatalogueClient.getEvents();
    }

    public Map<String, Module> getModuleMap() {
        return learningCatalogueClient.getReportingModules();
    }

    public Map<String, Module> getModuleMapForCourseIds(List<String> courseIds) {
        return learningCatalogueClient.getModulesForCourseIds(courseIds);
    }
}
