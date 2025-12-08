package uk.gov.cshr.report.domain.report;

import lombok.Getter;

@Getter
public enum ReportType {
    COURSE_COMPLETIONS("Course completions"),
    REGISTERED_LEARNERS("Registered Learners");

    private final String name;

    ReportType(String name) {
        this.name = name;
    }
}
