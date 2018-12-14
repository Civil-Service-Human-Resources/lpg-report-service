package uk.gov.cshr.report.domain;

import lombok.Data;

@Data
public class LearnerRecordSummary {
    private String courseIdentifier;
    private String courseName;
    private String moduleIdentifier;
    private String moduleName;
    private Integer timeTaken;
    private String type;
    private Integer completed;
    private Integer inProgress;
    private Integer notStarted;
}
