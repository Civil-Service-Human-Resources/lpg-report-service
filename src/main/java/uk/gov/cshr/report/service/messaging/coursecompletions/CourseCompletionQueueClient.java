package uk.gov.cshr.report.service.messaging.coursecompletions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;
import uk.gov.cshr.report.service.messaging.ObjectMapperQueueClient;

@Service
@Slf4j
public class CourseCompletionQueueClient extends ObjectMapperQueueClient<CourseCompletionMessage, CourseCompletionEvent> {

    protected CourseCompletionQueueClient(ObjectMapper objectMapper,
                                          CourseCompletionsMessageConverter converter,
                                          CourseCompletionEventRepository repository) {
        super(objectMapper, converter, repository, new TypeReference<>() { });
    }

    @Override
    @JmsListener(destination = "${app.messaging.queues.course-completions.name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage (String message) {
        this.convertAndSave(message);
    }
}
