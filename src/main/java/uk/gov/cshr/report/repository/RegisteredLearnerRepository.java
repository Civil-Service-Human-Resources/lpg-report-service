package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.cshr.report.domain.RegisteredLearner;

import java.time.ZonedDateTime;
import java.util.Collection;

public interface RegisteredLearnerRepository extends CrudRepository<RegisteredLearner, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.email = :email, rl.updatedTimestamp = :updatedTimestamp
        WHERE rl.uid = :uid
    """)
    int updateEmail(String uid, String email, ZonedDateTime updatedTimestamp);

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
