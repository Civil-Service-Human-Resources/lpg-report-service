package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.CompleteProfileMessage;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

@Component
@Getter
public class CreateRegisteredLearnerService extends RegisteredLearnerMessageService<CompleteProfileMessage> {

    private final RegisteredLearnerRepository registeredLearnerRepository;
    private final RegisteredLearnersMessageConverter registeredLearnersMessageConverter;

    public CreateRegisteredLearnerService(ObjectMapper objectMapper, RegisteredLearnerRepository registeredLearnerRepository, RegisteredLearnersMessageConverter registeredLearnersMessageConverter) {
        super(objectMapper, RegisteredLearnerOperation.CREATE, RegisteredLearnerDataType.LEARNER_PROFILE, CompleteProfileMessage.class);
        this.registeredLearnerRepository = registeredLearnerRepository;
        this.registeredLearnersMessageConverter = registeredLearnersMessageConverter;
    }

    @Override
    public void processConvertedMessage(CompleteProfileMessage message) {
        RegisteredLearner learner = registeredLearnersMessageConverter.convert(message);
        registeredLearnerRepository.save(learner);
    }

}
