package uk.gov.cshr.report.domain.learnerrecord;

import lombok.Data;

@Data
public class CourseRecord {
    private String courseId;
    private String learner;
    private String state;
    private String lastUpdated;
    private String courseTitle;
}
