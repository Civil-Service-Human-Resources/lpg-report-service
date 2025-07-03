package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;

import java.time.ZonedDateTime;
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

    @Transactional
    public int activateLearners(Collection<String> uids, ZonedDateTime updatedTimestamp) {
        log.debug("activateLearners: Activating learners with uids : {}, updatedTimestamp: {}", uids, updatedTimestamp);
        return registeredLearnerRepository.activate(uids, updatedTimestamp);
    }

    public int deactivateLearners(Collection<String> uids) {
        log.debug("Deactivating learners with uids : {}", uids);
        return registeredLearnerRepository.deactivate(uids);
    }
}
