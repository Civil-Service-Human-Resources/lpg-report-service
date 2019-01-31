package uk.gov.cshr.report.dto.learnerrecord;

import lombok.Data;

@Data
public class ModuleRecord {
    private String moduleId;
    private String state;
    private String learner;
    private String stateChangeDate;
}
