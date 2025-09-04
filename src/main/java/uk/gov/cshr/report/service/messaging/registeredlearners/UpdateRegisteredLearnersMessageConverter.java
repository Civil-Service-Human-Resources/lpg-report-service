package uk.gov.cshr.report.service.messaging.registeredlearners;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.utils.ClockConfig;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerProfile;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.UpdateProfileMessage;

import java.time.Clock;
import java.util.Optional;

@Slf4j
@Component
public class UpdateRegisteredLearnersMessageConverter {

    private final RegisteredLearnerRepository repository;
    private final ClockConfig clockConfig;

    public UpdateRegisteredLearnersMessageConverter(RegisteredLearnerRepository repository,
                                                    ClockConfig clockConfig) {
        this.repository = repository;
        this.clockConfig = clockConfig;
    }

    public RegisteredLearner convert(UpdateProfileMessage message) {
        Clock clock = clockConfig.getClock();

        RegisteredLearnerProfile profile = message.getMetadata().getData();
        Optional<RegisteredLearner> byIdOpt = repository.findById(profile.getUid());
        RegisteredLearner registeredLearner = byIdOpt.get();

        if(StringUtils.isNotBlank(profile.getEmail())) {
            registeredLearner.setEmail(profile.getEmail());
        }

        if(StringUtils.isNotBlank(profile.getFullName())) {
            registeredLearner.setFullName(profile.getFullName());
        }

        if(profile.getGradeId() == null) {
            registeredLearner.setGradeId(null);
        } else {
            registeredLearner.setGradeId(profile.getGradeId());
        }
        if(StringUtils.isBlank(profile.getGradeName())) {
            registeredLearner.setGradeName(null);
        } else {
            registeredLearner.setGradeName(profile.getGradeName());
        }

        if(profile.getOrganisationId() == null) {
            registeredLearner.setOrganisationId(null);
        } else {
            registeredLearner.setOrganisationId(profile.getOrganisationId());
        }
        if(StringUtils.isBlank(profile.getOrganisationName())) {
            registeredLearner.setOrganisationName(null);
        } else {
            registeredLearner.setOrganisationName(profile.getOrganisationName());
        }

        if(profile.getProfessionId() == null) {
            registeredLearner.setProfessionId(null);
        } else {
            registeredLearner.setProfessionId(profile.getProfessionId());
        }
        if(StringUtils.isBlank(profile.getProfessionName())) {
            registeredLearner.setProfessionName(null);
        } else {
            registeredLearner.setProfessionName(profile.getProfessionName());
        }

        registeredLearner.setUpdatedTimestamp(message.getMessageTimestamp().atZone(clock.getZone()));
        log.debug("registeredLearner: {}", registeredLearner);
        return registeredLearner;
    }
}
