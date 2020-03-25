package uk.gov.cshr.report.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder({"learnerId", "name", "email", "department", "profession", "otherAreasOfWork", "grade", "courseId",
        "courseTitle", "courseTopicId", "paidFor", "required", "status", "updatedAt", "completedAt"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CourseReportRow {
    private String learnerId;
    private String name;
    private String email;
    private String department;
    private String profession;
    private String otherAreasOfWork;
    private String grade;

    private String courseId;
    private String courseTitle;
    private String courseTopicId;
    private boolean required;
    private boolean paidFor;

    private String status;
    private String updatedAt;
    private String completedAt;
}
