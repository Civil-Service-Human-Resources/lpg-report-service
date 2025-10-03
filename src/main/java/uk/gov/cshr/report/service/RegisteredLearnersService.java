package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOrganisationDelete;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOrganisationUpdate;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class RegisteredLearnersService {

    private final RegisteredLearnerRepository registeredLearnerRepository;
    private final Clock clock;

    public RegisteredLearnersService(RegisteredLearnerRepository registeredLearnerRepository, Clock clock) {
        this.registeredLearnerRepository = registeredLearnerRepository;
        this.clock = clock;
    }

    @Transactional
    public int updateEmail(String uid, String email, ZonedDateTime updatedTimestamp) {
        log.info("updateEmail: Updating learner email with uid: {}, email: {}, updatedTimestamp: {}", uid, email, updatedTimestamp);
        return registeredLearnerRepository.updateEmail(uid, email, updatedTimestamp);
    }

    @Transactional
    public int deleteOrganisation(RegisteredLearnerOrganisationDelete registeredLearnerOrganisationDelete, ZonedDateTime updatedTimestamp) {
        log.info("deleteOrganisation: Deleting learner's organisation ids: {}, updatedTimestamp: {}",
                registeredLearnerOrganisationDelete.getOrganisationIds(), updatedTimestamp);
        return registeredLearnerRepository.deleteOrganisation(registeredLearnerOrganisationDelete.getOrganisationIds(), updatedTimestamp);
    }

    @Transactional
    public int updateOrganisation(List<RegisteredLearnerOrganisationUpdate> data, ZonedDateTime updatedTimestamp) {
        log.info("updateOrganisation: Updating organisations for {}, updatedTimestamp: {}", data, updatedTimestamp);
        AtomicInteger count = new AtomicInteger();
        data.forEach(ro ->  {
            int result = registeredLearnerRepository.updateOrganisation(ro.getOrganisationId(), ro.getOrganisationName(), updatedTimestamp);
            count.set(count.get() + result);
        });
        int totalCount = count.get();
        log.info("updateOrganisation: {} records updated", totalCount);
        return totalCount;
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
        ZonedDateTime updatedTimestamp = ZonedDateTime.now(clock);
        return registeredLearnerRepository.deactivate(uids, updatedTimestamp);
    }
}
