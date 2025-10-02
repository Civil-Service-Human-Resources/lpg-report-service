package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.exception.MessageProcessingException;
import uk.gov.cshr.report.service.RegisteredLearnersService;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.*;

import java.time.Clock;
import java.time.ZonedDateTime;

@Slf4j
@Component
@Getter
public class RegisteredLearnerOrganisationDeleteService extends
        RegisteredLearnerMessageService<RegisteredLearnerOrganisationDeleteMessage> {

    private final RegisteredLearnersService registeredLearnersService;
    private final Clock clock;

    public RegisteredLearnerOrganisationDeleteService(ObjectMapper objectMapper,
                                                      RegisteredLearnersService registeredLearnersService,
                                                      Clock clock) {
        super(objectMapper, RegisteredLearnerOperation.DELETE, RegisteredLearnerDataType.ORGANISATION,
                RegisteredLearnerOrganisationDeleteMessage.class);
        this.registeredLearnersService = registeredLearnersService;
        this.clock = clock;
    }

    @Override
    public void processConvertedMessage(RegisteredLearnerOrganisationDeleteMessage message) {
        log.debug("processConvertedMessage: message: {}", message);
        RegisteredLearnerOrganisationDelete data = message.getMetadata().getData();
        log.debug("processConvertedMessage: data: {}", data);
        if(data != null && data.getOrganisationIds() != null && data.getOrganisationIds().size() != 0) {
            ZonedDateTime zonedDateTime = message.getMessageTimestamp().atZone(clock.getZone());
            log.info("processConvertedMessage: Deleting learner's organisation for registeredLearnersOrganisation: {}, updatedTimestamp: {}",
                    data, zonedDateTime);
            registeredLearnersService.deleteOrganisation(data, zonedDateTime);
            log.info("processConvertedMessage: Deleted learner's organisation for registeredLearnersOrganisation: {}, updatedTimestamp: {}",
                    data, zonedDateTime);
        } else {
            log.error("processConvertedMessage: Unexpected learner organisation deletion data : {}", data);
            throw new MessageProcessingException("Unexpected registered learner organisation deletion data: " + data);
        }
    }
}
