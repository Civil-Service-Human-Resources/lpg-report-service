package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.exception.MessageProcessingException;
import uk.gov.cshr.report.service.RegisteredLearnersService;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOrganisationUpdate;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOrganisationUpdateMessage;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Getter
public class RegisteredLearnerOrganisationUpdateService extends
        RegisteredLearnerMessageService<RegisteredLearnerOrganisationUpdateMessage> {

    private final RegisteredLearnersService registeredLearnersService;
    private final Clock clock;

    public RegisteredLearnerOrganisationUpdateService(ObjectMapper objectMapper,
                                                      RegisteredLearnersService registeredLearnersService,
                                                      Clock clock) {
        super(objectMapper, RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.ORGANISATION,
                RegisteredLearnerOrganisationUpdateMessage.class);
        this.registeredLearnersService = registeredLearnersService;
        this.clock = clock;
    }

    @Override
    public void processConvertedMessage(RegisteredLearnerOrganisationUpdateMessage message) {
        log.debug("processConvertedMessage: message: {}", message);
        List<RegisteredLearnerOrganisationUpdate> data = message.getMetadata().getData();
        log.debug("processConvertedMessage: data: {}", data);
        if(data != null && data.size() > 0) {
            LocalDateTime timestamp = message.getMessageTimestamp();
            log.info("processConvertedMessage: Updating learner's organisations for registeredLearnersOrganisation: {}, updatedTimestamp: {}",
                    data, timestamp);
            registeredLearnersService.updateOrganisation(data, timestamp);
            log.info("processConvertedMessage: Update learner's organisation for registeredLearnersOrganisation: {}, updatedTimestamp: {}",
                    data, timestamp);
        } else {
            log.error("processConvertedMessage: Unexpected learner organisation update data : {}", data);
            throw new MessageProcessingException("Unexpected registered learner organisation update data: " + data);
        }
    }
}
