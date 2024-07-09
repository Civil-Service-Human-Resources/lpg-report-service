package uk.gov.cshr.report.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "course_completion_events")
public class CourseCompletionEvent {

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
    private String organisationAbbreviation;

    @Column(nullable = false)
    private Integer professionId;

    @Column
    private String professionName;

    @Column
    private Integer gradeId;

    @Column
    private String gradeCode;



    public CourseCompletionEvent(String externalId, String userId, String userEmail, String courseId, String courseTitle,
                                 LocalDateTime eventTimestamp, Integer organisationId, String organisationAbbreviation, Integer professionId, String professionName, Integer gradeId, String gradeCode) {
        this.externalId = externalId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.eventTimestamp = eventTimestamp;
        this.organisationId = organisationId;
        this.organisationAbbreviation = organisationAbbreviation;
        this.professionId = professionId;
        this.professionName = professionName;
        this.gradeId = gradeId;
        this.gradeCode = gradeCode;
    }

    public CourseCompletionEvent() {
    }

}
