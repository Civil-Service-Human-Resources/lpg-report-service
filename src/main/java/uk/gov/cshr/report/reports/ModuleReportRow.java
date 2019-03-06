package uk.gov.cshr.report.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "learnerId", "name", "email", "department", "profession", "otherAreasOfWork", "grade", "courseId",
        "courseTitle", "courseTopicId", "moduleId", "moduleTitle","learningProvider", "required", "status", "date" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleReportRow {
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
    private String moduleId;
    private String moduleTitle;
    private String moduleType;
    private boolean required;

    private String status;
    private String date;
}
