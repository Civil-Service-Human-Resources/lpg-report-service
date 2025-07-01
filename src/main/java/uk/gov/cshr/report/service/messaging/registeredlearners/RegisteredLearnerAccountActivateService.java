package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.exception.MessageProcessingException;
import uk.gov.cshr.report.service.RegisteredLearnersService;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerAccountActivate;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerAccountActivateMessage;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@Getter
public class RegisteredLearnerAccountActivateService
        extends RegisteredLearnerMessageService<RegisteredLearnerAccountActivateMessage> {

    private final RegisteredLearnersService registeredLearnersService;
    private final Clock clock;

    public RegisteredLearnerAccountActivateService(ObjectMapper objectMapper,
                                                   RegisteredLearnersService registeredLearnersService,
                                                   Clock clock) {
        super(objectMapper, RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.ACCOUNT_ACTIVATE,
                RegisteredLearnerAccountActivateMessage.class);
        this.registeredLearnersService = registeredLearnersService;
        this.clock = clock;
    }

    @Override
    public void processConvertedMessage(RegisteredLearnerAccountActivateMessage message) {
        RegisteredLearnerAccountActivate data = message.getMetadata().getData();
        String uid = data.getUid();
        if(data.getActive()) {
            ZonedDateTime zonedDateTime = message.getMessageTimestamp().atZone(clock.getZone());
            registeredLearnersService.activateLearners(List.of(uid), zonedDateTime);
        } else {
            log.error("Unexpected registered learner activation data for uid: {}", uid);
            throw new MessageProcessingException("Unexpected registered learner activation data: " + data);
        }
    }
}
