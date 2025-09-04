package uk.gov.cshr.report.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public class ReportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_request_id")
    private Long reportRequestId;

    @Column(name = "requester_id", length = 36)
    private String requesterId;

    @Column(name = "requester_email", length = 100)
    private String requesterEmail;

    @Column(name = "requested_timestamp", nullable = false)
    private ZonedDateTime requestedTimestamp;

    @Column(name = "completed_timestamp")
    private ZonedDateTime completedTimestamp;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportRequestStatus status;

    @Column(name = "requester_full_name")
    private String fullName;

    @Column(name = "url_slug", nullable = false)
    private String urlSlug;

    @Column(name = "download_base_url", nullable = false)
    private String downloadBaseUrl;

    @Column(name = "times_downloaded", nullable = false)
    private Integer timesDownloaded = 0;

    public ReportRequest(String requesterId, String requesterEmail, ZonedDateTime requestedTimestamp,
                         ReportRequestStatus status, String fullName, String urlSlug, String downloadBaseUrl) {
        this.requesterId = requesterId;
        this.requesterEmail = requesterEmail;
        this.requestedTimestamp = requestedTimestamp;
        this.status = status;
        this.fullName = fullName;
        this.urlSlug = urlSlug;
        this.downloadBaseUrl = downloadBaseUrl;
    }

    @JsonIgnore
    public String getFullDownloadUrl() {
        return String.format("%s/%s", downloadBaseUrl, urlSlug);
    }
}
