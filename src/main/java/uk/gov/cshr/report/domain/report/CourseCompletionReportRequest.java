package uk.gov.cshr.report.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import uk.gov.cshr.report.service.reportRequests.export.ExportCsvType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Table(name = "course_completion_report_requests")
@Setter
@ToString
@NoArgsConstructor
public class CourseCompletionReportRequest extends OrganisationalReportRequest implements IDownloadableReportRequest {

    @Column(name = "from_date", nullable = false)
    private LocalDateTime fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDateTime toDate;

    @Type(ListArrayType.class)
    @Column(name = "course_ids", nullable = false, columnDefinition = "text[]")
    private List<String> courseIds;

    @Type(ListArrayType.class)
    @Column(name = "profession_ids", columnDefinition = "int[]")
    private List<Integer> professionIds;

    @Type(ListArrayType.class)
    @Column(name = "grade_ids", columnDefinition = "int[]")
    private List<Integer> gradeIds;

    public CourseCompletionReportRequest(String requesterId, String requesterEmail, LocalDateTime requestedTimestamp,
                                         ReportRequestStatus status, LocalDateTime fromDate, LocalDateTime toDate, List<String> courseIds,
                                         List<Integer> organisationIds, List<Integer> professionIds, List<Integer> gradeIds,
                                         String requesterTimezone, String fullName, String urlSlug, String downloadBaseUrl, boolean detailed) {
        super(requesterId, requesterEmail, requestedTimestamp, status, fullName, requesterTimezone, urlSlug, downloadBaseUrl, organisationIds, detailed);
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.courseIds = courseIds;
        this.professionIds = professionIds;
        this.gradeIds = gradeIds;
    }

    @JsonIgnore
    public String getFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("course_completions_%s_from_%s_to_%s", getReportRequestId(),
                getFromDate().format(formatter), getToDate().format(formatter));
    }

    @JsonIgnore
    public ReportType getReportType() {
        return ReportType.COURSE_COMPLETIONS;
    }

    public ExportCsvType getExportCsvType() {
        return isDetailedExport() ? ExportCsvType.COURSE_COMPLETIONS_DETAILED : ExportCsvType.COURSE_COMPLETIONS;
    }
}
