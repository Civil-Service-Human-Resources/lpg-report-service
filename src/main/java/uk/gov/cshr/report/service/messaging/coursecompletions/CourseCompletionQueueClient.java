package uk.gov.cshr.report.service.messaging.coursecompletions;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.service.CourseCompletionService;
import uk.gov.cshr.report.service.messaging.MessageConverter;
import uk.gov.cshr.report.service.messaging.ObjectMapperQueueClient;
import uk.gov.cshr.report.service.messaging.model.Message;

@Service
@Slf4j
public class CourseCompletionQueueClient extends ObjectMapperQueueClient {

    private final CourseCompletionService courseCompletionService;

    protected CourseCompletionQueueClient(MessageConverter converter, CourseCompletionService courseCompletionService) {
        super(converter);
        this.courseCompletionService = courseCompletionService;
    }

    @Override
    @JmsListener(destination = "${app.messaging.queues.course-completions.name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage (String message) {
        Message<CourseCompletionMessage> courseCompletionMessage = super.converter.convert(message, new TypeReference<>() { });
        courseCompletionService.saveCourseCompletionMessage(courseCompletionMessage);
    }
}
