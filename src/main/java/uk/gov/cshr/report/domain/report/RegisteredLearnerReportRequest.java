package uk.gov.cshr.report.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Table(name = "registered_learners_report_requests")
@Setter
@ToString
@NoArgsConstructor
public class RegisteredLearnerReportRequest extends OrganisationalReportRequest {

    public RegisteredLearnerReportRequest(String requesterId, String requesterEmail, LocalDateTime requestedTimestamp,
                                          ReportRequestStatus status, String fullName, String urlSlug, String downloadBaseUrl,
                                          List<Integer> organisationIds) {
        super(requesterId, requesterEmail, requestedTimestamp, status, fullName, urlSlug, downloadBaseUrl, organisationIds);
    }

    @JsonIgnore
    public String getFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("registered_learners_%s_%s", getReportRequestId(),
                getRequestedTimestamp().format(formatter));
    }

}
