package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.cshr.report.domain.RegisteredLearner;

import java.util.Collection;

public interface RegisteredLearnerRepository extends CrudRepository<RegisteredLearner, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE RegisteredLearner rl
        SET rl.active = false
        WHERE rl.uid in :uids
    """)
    int deactivate(Collection<String> uids);

    int deleteAllByUidIn(Collection<String> ids);
}
