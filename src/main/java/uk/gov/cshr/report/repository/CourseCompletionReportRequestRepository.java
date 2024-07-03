package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;

public interface CourseCompletionReportRequestRepository extends JpaRepository<CourseCompletionReportRequest, Long> {

}
