package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.model.Message;
import uk.gov.cshr.report.service.messaging.registeredlearners.RegisteredLearnersMessageConverter;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.IRegisteredLearnerMessageMetadata;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.util.Collection;

@Service
@Slf4j
public class RegisteredLearnersService {

    private final RegisteredLearnerRepository registeredLearnerRepository;
    private final RegisteredLearnersMessageConverter registeredLearnersMessageConverter;

    public RegisteredLearnersService(RegisteredLearnerRepository registeredLearnerRepository, RegisteredLearnersMessageConverter registeredLearnersMessageConverter) {
        this.registeredLearnerRepository = registeredLearnerRepository;
        this.registeredLearnersMessageConverter = registeredLearnersMessageConverter;
    }

    public int deleteLearners(Collection<String> uids) {
        log.debug("Deleting registered learners with uids : {}", uids);
        return registeredLearnerRepository.deleteAllByUidIn(uids);
    }

    public int deactivateLearners(Collection<String> uids) {
        log.debug("Deactivating learners with uids : {}", uids);
        return registeredLearnerRepository.deactivate(uids);
    }

    public void processRegisteredLearnerMessage(Message<IRegisteredLearnerMessageMetadata> registeredLearnerMessage) {
        IRegisteredLearnerMessageMetadata metadata = registeredLearnerMessage.getMetadata();
        RegisteredLearner learner;
        if (metadata.getDataType() == RegisteredLearnerDataType.LEARNER_PROFILE) {
            if (metadata.getOperation() == RegisteredLearnerOperation.CREATE) {
                learner = registeredLearnersMessageConverter.convert(registeredLearnerMessage);
            } else {
                throw new IllegalStateException("Unexpected registered learner operation: " + metadata.getOperation());
            }
        } else {
            throw new IllegalStateException("Unexpected registered learner data type: " + metadata.getDataType());
        }
        registeredLearnerRepository.save(learner);
    }
}
