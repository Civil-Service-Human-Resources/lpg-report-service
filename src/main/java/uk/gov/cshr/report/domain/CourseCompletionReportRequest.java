package uk.gov.cshr.report.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @ElementCollection
    @CollectionTable(name = "course_completion_report_requests_course_ids", joinColumns = @JoinColumn(name = "report_request_id"))
    @Column(name = "course_ids", nullable = false)
    private List<String> courseIds;

    @ElementCollection
    @Column(name = "organisation_ids", nullable = false)
    private List<Integer> organisationIds;

    @ElementCollection
    @Column(name = "profession_ids")
    private List<Integer> professionIds;

    @ElementCollection
    @Column(name = "grade_ids")
    private List<Integer> gradeIds;


}