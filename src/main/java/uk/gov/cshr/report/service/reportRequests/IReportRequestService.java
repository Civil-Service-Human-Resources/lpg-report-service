package uk.gov.cshr.report.service.reportRequests;

import uk.gov.cshr.report.domain.report.IDownloadableReportRequest;
import uk.gov.cshr.report.domain.report.ReportableData;

import java.util.List;

public interface IReportRequestService<T extends ReportableData, R extends IDownloadableReportRequest> {

    List<T> getReportRequestData(R reportRequest);

}
