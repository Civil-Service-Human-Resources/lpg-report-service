package uk.gov.cshr.report.domain.catalogue;

import lombok.Data;

@Data
public class Event {
    private String id;
    private Module module;
    private LearningProvider learningProvider;
    private String location;
    private String eventDate;
}
