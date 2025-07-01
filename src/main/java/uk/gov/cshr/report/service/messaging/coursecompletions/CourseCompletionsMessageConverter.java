package uk.gov.cshr.report.service.messaging.coursecompletions;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.service.messaging.MessageConverter;
import uk.gov.cshr.report.service.messaging.model.Message;

@Component
public class CourseCompletionsMessageConverter {

    private final MessageConverter converter;

    public CourseCompletionsMessageConverter(MessageConverter converter) {
        this.converter = converter;
    }

    public CourseCompletionEvent convert(String message) {
        Message<CourseCompletionMessage> convertedMessage = converter.convert(message, new TypeReference<>() {});
        CourseCompletionMessage metadata = convertedMessage.getMetadata();
        return new CourseCompletionEvent(convertedMessage.getMessageId(), metadata.getUserId(), metadata.getUserEmail(),
                metadata.getCourseId(), metadata.getCourseTitle(), metadata.getCompletionDate(), metadata.getOrganisationId(),
                metadata.getOrganisationName(),
                metadata.getProfessionId(), metadata.getProfessionName(), metadata.getGradeId(), metadata.getGradeName());
    }
}
