package uk.gov.cshr.report.service.messaging.registeredlearners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.service.messaging.IMessageProcessor;

@Service
@Slf4j
public class RegisteredLearnerQueueClient implements IMessageProcessor {

    private final RegisteredLearnerConverterService registeredLearnerConverterService;

    public RegisteredLearnerQueueClient(RegisteredLearnerConverterService registeredLearnerConverterService) {
        this.registeredLearnerConverterService = registeredLearnerConverterService;
    }

    @Override
    @JmsListener(destination = "${app.messaging.queues.registered-learners.name}", containerFactory = "jmsListenerContainerFactory")
    public void processMessage(String message) {
        registeredLearnerConverterService.processMessage(message);
    }
}
