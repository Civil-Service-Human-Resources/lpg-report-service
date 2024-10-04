package uk.gov.cshr.report.service.messaging.coursecompletions;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.service.messaging.MessageToEntityConverter;
import uk.gov.cshr.report.service.messaging.model.Message;

@Component
public class CourseCompletionsMessageConverter implements MessageToEntityConverter<CourseCompletionMessage, CourseCompletionEvent> {
    @Override
    public CourseCompletionEvent convert(Message<CourseCompletionMessage> message) {
        CourseCompletionMessage metadata = message.getMetadata();
        return new CourseCompletionEvent(message.getMessageId(), metadata.getUserId(), metadata.getUserEmail(),
                metadata.getCourseId(), metadata.getCourseTitle(), metadata.getCompletionDate(), metadata.getOrganisationId(),
                metadata.getOrganisationName(),
                metadata.getProfessionId(), metadata.getProfessionName(), metadata.getGradeId(), metadata.getGradeName());
    }
}
