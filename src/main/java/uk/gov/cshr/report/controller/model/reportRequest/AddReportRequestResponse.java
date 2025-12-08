package uk.gov.cshr.report.controller.model.reportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AddReportRequestResponse {
    private final Boolean addedSuccessfully;
    private String details;
}
