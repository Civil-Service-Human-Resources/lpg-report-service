package uk.gov.cshr.report.domain.learnerrecord;

import lombok.Data;

@Data
public class ModuleRecord {
    private String moduleId;
    private String state;
    private String learner;
    private String stateChangeDate;
    private String completedAt;
    private String moduleTitle;
    private String moduleType;
    private String courseId;
    private String courseTitle;
}
