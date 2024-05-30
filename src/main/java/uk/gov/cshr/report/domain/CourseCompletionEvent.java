package uk.gov.cshr.report.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private ZonedDateTime eventTimestamp;

    @Column(nullable = false)
    private Integer organisationId;

    @Column(nullable = false)
    private Integer professionId;

    @Column
    private Integer gradeId;

    public CourseCompletionEvent(String externalId, String userId, String userEmail, String courseId, String courseTitle,
                                 LocalDateTime eventTimestamp, Integer organisationId, Integer professionId, Integer gradeId) {
        this.externalId = externalId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.eventTimestamp = eventTimestamp.atZone(ZoneId.systemDefault());
        this.organisationId = organisationId;
        this.professionId = professionId;
        this.gradeId = gradeId;
    }

    public CourseCompletionEvent() {
    }

}
