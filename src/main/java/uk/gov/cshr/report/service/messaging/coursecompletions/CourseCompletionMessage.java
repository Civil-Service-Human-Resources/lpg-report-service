package uk.gov.cshr.report.service.messaging.coursecompletions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.gov.cshr.report.service.messaging.model.IMessageMetadata;

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
    private final String organisationAbbreviation;
    private final Integer professionId;
    private final String professionName;
    private final Integer gradeId;
    private final String gradeCode;

    @Override
    public String getQueue() {
        return "coursecompletions";
    }
}
