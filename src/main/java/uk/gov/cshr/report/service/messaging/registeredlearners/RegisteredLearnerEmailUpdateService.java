package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.exception.MessageProcessingException;
import uk.gov.cshr.report.service.RegisteredLearnersService;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerEmailUpdate;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerEmailUpdateMessage;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Component
@Getter
public class RegisteredLearnerEmailUpdateService extends
        RegisteredLearnerMessageService<RegisteredLearnerEmailUpdateMessage> {

    private final RegisteredLearnersService registeredLearnersService;
    private final Clock clock;

    public RegisteredLearnerEmailUpdateService(ObjectMapper objectMapper,
                                               RegisteredLearnersService registeredLearnersService,
                                               Clock clock) {
        super(objectMapper, RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.EMAIL_UPDATE,
                RegisteredLearnerEmailUpdateMessage.class);
        this.registeredLearnersService = registeredLearnersService;
        this.clock = clock;
    }

    @Override
    public void processConvertedMessage(RegisteredLearnerEmailUpdateMessage message) {
        log.debug("processConvertedMessage: message: {}", message);
        RegisteredLearnerEmailUpdate data = message.getMetadata().getData();
        log.debug("processConvertedMessage: data: {}", data);
        String uid = data.getUid();
        String email = data.getEmail();
        if(StringUtils.isNotBlank(email)) {
            LocalDateTime localDateTime = message.getMessageTimestamp();
            log.info("processConvertedMessage: Updating learner email {} for uid : {}, updatedTimestamp: {}", email, uid, localDateTime);
            registeredLearnersService.updateEmail(uid, email, localDateTime);
            log.info("processConvertedMessage: Updated learner email {} for uid : {}, updatedTimestamp: {}", email, uid, localDateTime);
        } else {
            log.error("processConvertedMessage: Unexpected registered learner email update data for uid: {}", uid);
            throw new MessageProcessingException("Unexpected registered learner email update data: " + data);
        }
    }
}
