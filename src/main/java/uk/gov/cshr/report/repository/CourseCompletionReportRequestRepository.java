package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;

import java.util.List;

public interface CourseCompletionReportRequestRepository extends JpaRepository<CourseCompletionReportRequest, Long> {
    List<CourseCompletionReportRequest> findByRequesterIdAndStatus(String requestedId, String status);
    CourseCompletionReportRequest findByReportRequestId(Long reportRequestId);
    List<CourseCompletionReportRequest> findByStatus(String status);
}

