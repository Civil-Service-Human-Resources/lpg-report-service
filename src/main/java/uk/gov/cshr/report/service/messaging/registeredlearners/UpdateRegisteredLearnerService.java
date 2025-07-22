package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.UpdateProfileMessage;

@Component
@Getter
public class UpdateRegisteredLearnerService extends RegisteredLearnerMessageService<UpdateProfileMessage> {

    private final RegisteredLearnerRepository repository;
    private final UpdateRegisteredLearnersMessageConverter converter;

    public UpdateRegisteredLearnerService(ObjectMapper objectMapper, RegisteredLearnerRepository repository,
                                          UpdateRegisteredLearnersMessageConverter converter) {
        super(objectMapper, RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.LEARNER_PROFILE, UpdateProfileMessage.class);
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public void processConvertedMessage(UpdateProfileMessage message) {
        RegisteredLearner learner = converter.convert(message);
        repository.save(learner);
    }
}
