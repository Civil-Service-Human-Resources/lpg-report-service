package uk.gov.cshr.report.domain.report;

import java.time.LocalDateTime;

public interface IReportRequest {
    Long getReportRequestId();

    String getRequesterId();

    String getRequesterEmail();

    LocalDateTime getRequestedTimestamp();

    LocalDateTime getCompletedTimestamp();

    ReportRequestStatus getStatus();

    String getFullName();

    String getRequesterTimezone();

    String getUrlSlug();

    String getDownloadBaseUrl();

    Integer getTimesDownloaded();

    boolean isDetailedExport();

    void setStatus(ReportRequestStatus reportRequestStatus);

    void setCompletedTimestamp(LocalDateTime completedTimestamp);

    void setTimesDownloaded(Integer i);
}
