package uk.gov.cshr.report.service.messaging.registeredlearners;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.utils.ClockConfig;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.CompleteProfileMessage;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerProfile;

import java.time.Clock;

@Component
public class RegisteredLearnersMessageConverter {

    private final ClockConfig clockConfig;

    public RegisteredLearnersMessageConverter(ClockConfig clockConfig) {
        this.clockConfig = clockConfig;
    }

    public RegisteredLearner convert(CompleteProfileMessage message) {
        Clock clock = clockConfig.getClock();
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
