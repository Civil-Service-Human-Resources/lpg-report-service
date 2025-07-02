package uk.gov.cshr.report.service.messaging.coursecompletions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;
import uk.gov.cshr.report.service.messaging.IMessageProcessor;

@Service
@Slf4j
public class CourseCompletionQueueClient implements IMessageProcessor {

    private final CourseCompletionEventRepository repository;
    private final CourseCompletionsMessageConverter messageConverter;

    public CourseCompletionQueueClient(CourseCompletionEventRepository repository, CourseCompletionsMessageConverter messageConverter) {
        this.repository = repository;
        this.messageConverter = messageConverter;
    }

    @Override
    @JmsListener(destination = "${app.messaging.queues.course-completions.name}", containerFactory = "jmsListenerContainerFactory")
    public void processMessage(String message) {
        CourseCompletionEvent courseCompletionEvent = messageConverter.convert(message);
        repository.save(courseCompletionEvent);
    }
}
