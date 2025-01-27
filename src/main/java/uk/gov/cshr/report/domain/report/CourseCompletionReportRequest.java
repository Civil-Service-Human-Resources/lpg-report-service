package uk.gov.cshr.report.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Table(name = "course_completion_report_requests")
@Setter
@ToString
@NoArgsConstructor
public class CourseCompletionReportRequest {
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
    private CourseCompletionReportRequestStatus status;

    @Column(name = "from_date", nullable = false)
    private ZonedDateTime fromDate;

    @Column(name = "to_date", nullable = false)
    private ZonedDateTime toDate;

    @Type(ListArrayType.class)
    @Column(name = "course_ids", nullable = false, columnDefinition = "text[]")
    private List<String> courseIds;

    @Type(ListArrayType.class)
    @Column(name = "organisation_ids", nullable = false, columnDefinition = "int[]")
    private List<Integer> organisationIds;

    @Type(ListArrayType.class)
    @Column(name = "profession_ids", columnDefinition = "int[]")
    private List<Integer> professionIds;

    @Type(ListArrayType.class)
    @Column(name = "grade_ids", columnDefinition = "int[]")
    private List<Integer> gradeIds;

    @Column(name = "requester_timezone")
    private String requesterTimezone;

    @Column(name = "requester_full_name")
    private String fullName;

    @Column(name = "url_slug", nullable = false)
    private String urlSlug;

    @Column(name = "download_base_url", nullable = false)
    private String downloadBaseUrl;

    @Column(name = "times_downloaded", nullable = false)
    private Integer timesDownloaded = 0;

    @Column(name = "detailed_export")
    private Boolean detailedExport = false;

    public CourseCompletionReportRequest(String requesterId, String requesterEmail, ZonedDateTime requestedTimestamp,
                                         CourseCompletionReportRequestStatus status, ZonedDateTime fromDate, ZonedDateTime toDate, List<String> courseIds,
                                         List<Integer> organisationIds, List<Integer> professionIds, List<Integer> gradeIds,
                                         String requesterTimezone, String fullName, String urlSlug, String downloadBaseUrl) {
        this.requesterId = requesterId;
        this.requesterEmail = requesterEmail;
        this.requestedTimestamp = requestedTimestamp;
        this.status = status;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.courseIds = courseIds;
        this.organisationIds = organisationIds;
        this.professionIds = professionIds;
        this.gradeIds = gradeIds;
        this.requesterTimezone = requesterTimezone;
        this.fullName = fullName;
        this.urlSlug = urlSlug;
        this.downloadBaseUrl = downloadBaseUrl;
    }

    @JsonIgnore
    public String getFullDownloadUrl() {
        return String.format("%s/%s", downloadBaseUrl, urlSlug);
    }

    @JsonIgnore
    public String getFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("course_completions_%s_from_%s_to_%s", getReportRequestId(),
                getFromDate().format(formatter), getToDate().format(formatter));
    }

}
