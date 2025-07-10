package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class RegisteredLearnersService {

    private final RegisteredLearnerRepository registeredLearnerRepository;

    public RegisteredLearnersService(RegisteredLearnerRepository registeredLearnerRepository) {
        this.registeredLearnerRepository = registeredLearnerRepository;
    }

    @Transactional
    public int updateEmail(String uid, String email, ZonedDateTime updatedTimestamp) {
        log.info("updateEmail: Updating learner email with uid: {}, email: {}, updatedTimestamp: {}", uid, email, updatedTimestamp);
        return registeredLearnerRepository.updateEmail(uid, email, updatedTimestamp);
    }

    public int deleteLearners(Collection<String> uids) {
        log.debug("Deleting registered learners with uids : {}", uids);
        return registeredLearnerRepository.deleteAllByUidIn(uids);
    }

    @Transactional
    public int activateLearners(String uid, ZonedDateTime updatedTimestamp) {
        log.info("activateLearners: Activating learner with uid : {}, updatedTimestamp: {}", uid, updatedTimestamp);
        return registeredLearnerRepository.activate(List.of(uid), updatedTimestamp);
    }

    public int deactivateLearners(Collection<String> uids) {
        log.debug("Deactivating learners with uids : {}", uids);
        return registeredLearnerRepository.deactivate(uids);
    }
}
