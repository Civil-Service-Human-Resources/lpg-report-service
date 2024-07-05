package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;

import java.util.List;

public interface CourseCompletionReportRequestRepository extends JpaRepository<CourseCompletionReportRequest, Long> {

    @Query("""
        select r from CourseCompletionReportRequest r
            where r.requesterId = :userId and r.status = :status
    """)

    List<CourseCompletionReportRequest> findAllByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);
}
