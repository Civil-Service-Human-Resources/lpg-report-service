package uk.gov.cshr.report.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;

import java.util.List;

@Data
@AllArgsConstructor
public class GetCourseCompletionReportRequestsResponse {
    private final List<CourseCompletionReportRequest> requests;
}
