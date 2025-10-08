package uk.gov.cshr.report.domain.report;

import uk.gov.cshr.report.service.reportRequests.export.ExportCsvType;

public interface IDownloadableReportRequest extends IReportRequest {

    ReportType getReportType();
    ExportCsvType getExportCsvType();
    String getFileName();

    default String getFullDownloadUrl() {
        return String.format("%s/%s", getDownloadBaseUrl(), getUrlSlug());
    }

}
