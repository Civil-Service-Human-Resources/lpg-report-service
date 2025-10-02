package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.cshr.report.domain.RegisteredLearner;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface RegisteredLearnerRepository extends CrudRepository<RegisteredLearner, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.email = :email, rl.organisationId = null, rl.organisationName = null, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid = :uid
    """)
    int updateEmail(String uid, String email, LocalDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.organisationId = null, rl.organisationName = null, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.organisationId in :organisationIds
    """)
    int deleteOrganisation(List<Long> organisationIds, LocalDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.active = true, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid in :uids
    """)
    int activate(Collection<String> uids, LocalDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.active = false, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid in :uids
    """)
    int deactivate(Collection<String> uids, LocalDateTime updatedTimestamp);

    int deleteAllByUidIn(Collection<String> ids);

    @Query("""
        select rl
        from RegisteredLearner rl
        where :organisationIds is null or rl.organisationId in :organisationIds
    """)
    List<RegisteredLearner> findAllByOrganisationIdIn(Collection<Integer> organisationIds);
}
