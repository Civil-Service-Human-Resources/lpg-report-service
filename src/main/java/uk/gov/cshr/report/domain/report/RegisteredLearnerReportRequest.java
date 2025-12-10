package uk.gov.cshr.report.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.cshr.report.service.reportRequests.export.ExportCsvType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Table(name = "registered_learners_report_requests")
@Setter
@ToString
@NoArgsConstructor
public class RegisteredLearnerReportRequest extends OrganisationalReportRequest implements IDownloadableReportRequest {

    public RegisteredLearnerReportRequest(String requesterId, String requesterEmail, LocalDateTime requestedTimestamp,
                                          ReportRequestStatus status, String fullName, String requesterTimezone, String urlSlug, String downloadBaseUrl,
                                          List<Integer> organisationIds, boolean detailedExport) {
        super(requesterId, requesterEmail, requestedTimestamp, status, fullName, requesterTimezone, urlSlug, downloadBaseUrl, organisationIds,
                detailedExport);
    }

    @Override
    public String getFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("registered_learners_%s_%s", getReportRequestId(),
                getRequestedTimestamp().format(formatter));
    }

    @JsonIgnore
    public ReportType getReportType() {
        return ReportType.REGISTERED_LEARNERS;
    }

    @Override
    public ExportCsvType getExportCsvType() {
        return isDetailedExport() ? ExportCsvType.REGISTERED_LEARNERS_DETAILED : ExportCsvType.REGISTERED_LEARNERS;
    }
}
