package uk.gov.cshr.report.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.gov.cshr.report.domain.report.ReportRequest;

import java.util.List;

@Data
@AllArgsConstructor
public class GetReportRequestsResponse<T extends ReportRequest> {
    private final List<T> requests;
}
