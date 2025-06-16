package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;

import java.util.Collection;

@Service
@Slf4j
public class RegisteredLearnersService {

    private final RegisteredLearnerRepository registeredLearnerRepository;

    public RegisteredLearnersService(RegisteredLearnerRepository registeredLearnerRepository) {
        this.registeredLearnerRepository = registeredLearnerRepository;
    }

    public int deleteLearners(Collection<String> uids) {
        log.debug("Deleting registered learners with uids : {}", uids);
        return registeredLearnerRepository.deleteAllByUidIn(uids);
    }

    public int deactivateLearners(Collection<String> uids) {
        log.debug("Deactivating learners with uids : {}", uids);
        return registeredLearnerRepository.deactivate(uids);
    }
}
