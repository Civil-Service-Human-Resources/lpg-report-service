package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequestStatus;

import java.util.List;
import java.util.Optional;

public interface CourseCompletionReportRequestRepository extends JpaRepository<CourseCompletionReportRequest, Long> {
    List<CourseCompletionReportRequest> findByRequesterIdAndStatusIn(String requestedId, List<CourseCompletionReportRequestStatus> status);
    List<CourseCompletionReportRequest> findByStatus(CourseCompletionReportRequestStatus status);
    Optional<CourseCompletionReportRequest> findByUrlSlug(String urlSlug);
}

