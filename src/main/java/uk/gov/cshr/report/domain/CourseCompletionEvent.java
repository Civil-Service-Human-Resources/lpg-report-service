package uk.gov.cshr.report.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uk.gov.cshr.report.domain.report.ReportableData;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "course_completion_events")
public class CourseCompletionEvent extends ReportableData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String externalId;

    @Column
    private String userId;

    @Column
    private String userEmail;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String courseTitle;

    @Column(nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(nullable = false)
    private Integer organisationId;

    @Column
    private String organisationName;

    @Column(nullable = false)
    private Integer professionId;

    @Column
    private String professionName;

    @Column
    private Integer gradeId;

    @Column
    private String gradeName;



    public CourseCompletionEvent(String externalId, String userId, String userEmail, String courseId, String courseTitle,
                                 LocalDateTime eventTimestamp, Integer organisationId, String organisationName,
                                 Integer professionId, String professionName, Integer gradeId, String gradeName) {
        this.externalId = externalId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.eventTimestamp = eventTimestamp;
        this.organisationId = organisationId;
        this.organisationName = organisationName;
        this.professionId = professionId;
        this.professionName = professionName;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
    }

    public CourseCompletionEvent() {
    }

}
