package uk.gov.cshr.report.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AddCourseCompletionReportRequestResponse {
    private final Boolean addedSuccessfully;
    private String details;
}
