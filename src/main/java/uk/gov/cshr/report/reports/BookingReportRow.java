package uk.gov.cshr.report.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.Instant;

@Data
@JsonPropertyOrder({ "learnerId", "name", "email", "department", "profession", "otherAreasOfWork", "grade", "courseId",
        "courseTitle", "moduleId", "moduleTitle","learningProvider", "required", "status" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingReportRow {
    private String learnerId;
    private String name;
    private String email;
    private String department;
    private String profession;
    private String otherAreasOfWork;
    private String grade;
    private String courseId;
    private String courseTitle;
    private String moduleId;
    private String moduleTitle;
    private String learningProvider;
    private boolean required;
    private String status;
    private String bookingTime;
    private String confirmationTime;
    private String cancellationTime;
    private String accessibilityOptions;
    private String bookingCancellationReason;
}
