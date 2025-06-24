package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.service.RegisteredLearnersService;
import uk.gov.cshr.report.service.messaging.MessageConverter;
import uk.gov.cshr.report.service.messaging.ObjectMapperQueueClient;
import uk.gov.cshr.report.service.messaging.model.Message;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.IRegisteredLearnerMessageMetadata;

@Service
@Slf4j
public class RegisteredLearnerQueueClient extends ObjectMapperQueueClient {

    private final RegisteredLearnersService registeredLearnersService;

    protected RegisteredLearnerQueueClient(MessageConverter converter, RegisteredLearnersService registeredLearnersService) {
        super(converter);
        this.registeredLearnersService = registeredLearnersService;
    }

    @Override
    @JmsListener(destination = "${app.messaging.queues.registered-learners.name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage (String message) {
        Message<IRegisteredLearnerMessageMetadata> registeredLearnerMessage = super.converter.convert(message, new TypeReference<>() { });
        registeredLearnersService.processRegisteredLearnerMessage(registeredLearnerMessage);
    }
}
