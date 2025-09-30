package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.reportRequests.IReportRequestService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
public class RegisteredLearnersService implements IReportRequestService<RegisteredLearner, RegisteredLearnerReportRequest> {

    private final RegisteredLearnerRepository registeredLearnerRepository;
    private final Clock clock;

    public RegisteredLearnersService(RegisteredLearnerRepository registeredLearnerRepository, Clock clock) {
        this.registeredLearnerRepository = registeredLearnerRepository;
        this.clock = clock;
    }

    @Transactional
    public int updateEmail(String uid, String email, LocalDateTime updatedTimestamp) {
        log.info("updateEmail: Updating learner email with uid: {}, email: {}, updatedTimestamp: {}", uid, email, updatedTimestamp);
        return registeredLearnerRepository.updateEmail(uid, email, updatedTimestamp);
    }

    public int deleteLearners(Collection<String> uids) {
        log.debug("Deleting registered learners with uids : {}", uids);
        return registeredLearnerRepository.deleteAllByUidIn(uids);
    }

    @Transactional
    public int activateLearners(String uid, LocalDateTime updatedTimestamp) {
        log.info("activateLearners: Activating learner with uid : {}, updatedTimestamp: {}", uid, updatedTimestamp);
        return registeredLearnerRepository.activate(List.of(uid), updatedTimestamp);
    }

    public int deactivateLearners(Collection<String> uids) {
        log.debug("Deactivating learners with uids : {}", uids);
        LocalDateTime updatedTimestamp = LocalDateTime.now(clock);
        return registeredLearnerRepository.deactivate(uids, updatedTimestamp);
    }

    @Override
    public List<RegisteredLearner> getReportRequestData(RegisteredLearnerReportRequest reportRequest) {
        List<Integer> organisationIds = reportRequest.getOrganisationIds();
        return registeredLearnerRepository.findAllByOrganisationIdIn(isEmpty(organisationIds) ? null : organisationIds);
    }
}
