package uk.gov.cshr.report.service.messaging.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public final class CourseCompletionMessage implements IMessageMetadata, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private final LocalDateTime completionDate;
    private final String userId;
    private final String userEmail;
    private final String courseId;
    private final String courseTitle;
    private final Integer organisationId;
    private final Integer professionId;
    private final Integer gradeId;

    @Override
    public String getQueue() {
        return "coursecompletions";
    }
}
