package uk.gov.cshr.report.client.learningCatalogue;

import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;

import java.util.Map;

public interface ILearningCatalogueClient {
    public Map<String, Event> getEvents();

    public Map<String, Module> getModulesForCourseIds(String courseIds);
}
