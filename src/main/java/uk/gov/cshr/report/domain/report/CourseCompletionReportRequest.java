package uk.gov.cshr.report.domain.report;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "course_completion_report_requests")
@Setter
@ToString
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
    private String status;

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

}
