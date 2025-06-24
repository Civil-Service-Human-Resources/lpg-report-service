package uk.gov.cshr.report.service.messaging.registeredlearners;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.service.messaging.MessageToEntityConverter;
import uk.gov.cshr.report.service.messaging.model.Message;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.IRegisteredLearnerMessageMetadata;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerProfile;

import java.time.Clock;

@Component
public class RegisteredLearnersMessageConverter implements MessageToEntityConverter<IRegisteredLearnerMessageMetadata, RegisteredLearner> {

    private final Clock clock;

    public RegisteredLearnersMessageConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public RegisteredLearner convert(Message<IRegisteredLearnerMessageMetadata> message) {
        RegisteredLearnerProfile profile = message.getMetadata().getData();
        return new RegisteredLearner(
                profile.getUid(),
                profile.getEmail(),
                true,
                profile.getFullName(),
                profile.getOrganisationId(),
                profile.getOrganisationName(),
                profile.getGradeId(),
                profile.getGradeName(),
                profile.getProfessionId(),
                profile.getProfessionName(),
                message.getMessageTimestamp().atZone(clock.getZone()),
                message.getMessageTimestamp().atZone(clock.getZone())
        );
    }
}
