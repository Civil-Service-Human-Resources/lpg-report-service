package uk.gov.cshr.report.service.reportRequests.export;

import lombok.Getter;

@Getter
public enum ExportCsvType {
    REGISTERED_LEARNERS(new RegisteredLearnerCsvType()),
    COURSE_COMPLETIONS(new CourseCompletionsStandardCsvType()),
    COURSE_COMPLETIONS_DETAILED(new CourseCompletionsDetailedCsvType()),;

    private final ICsvConfig config;

    ExportCsvType(ICsvConfig config) {
        this.config = config;
    }
}
