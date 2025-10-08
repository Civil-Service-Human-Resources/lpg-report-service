package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.cshr.report.domain.RegisteredLearner;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public interface RegisteredLearnerRepository extends CrudRepository<RegisteredLearner, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.email = :email, rl.organisationId = null, rl.organisationName = null, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid = :uid
    """)
    int updateEmail(String uid, String email, ZonedDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.organisationId = null, rl.organisationName = null, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.organisationId in :organisationIds
    """)
    int deleteOrganisation(List<Long> organisationIds, ZonedDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.organisationName = :organisationName, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.organisationId in :organisationId
    """)
    int updateOrganisation(Long organisationId, String organisationName, ZonedDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.active = true, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid in :uids
    """)
    int activate(Collection<String> uids, ZonedDateTime updatedTimestamp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.active = false, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid in :uids
    """)
    int deactivate(Collection<String> uids, ZonedDateTime updatedTimestamp);

    int deleteAllByUidIn(Collection<String> ids);
}
