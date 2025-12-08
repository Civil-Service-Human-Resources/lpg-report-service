package uk.gov.cshr.report.controller.mappers;

import uk.gov.cshr.report.controller.model.reportRequest.ReportRequestParams;
import uk.gov.cshr.report.domain.report.IDownloadableReportRequest;

public interface ReportParamsToRequestMapper <T extends IDownloadableReportRequest, R extends ReportRequestParams> {

    T buildReportRequest (R params);

}
