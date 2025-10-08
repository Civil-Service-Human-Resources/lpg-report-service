package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import uk.gov.cshr.report.domain.report.IDownloadableReportRequest;
import uk.gov.cshr.report.domain.report.ReportRequestStatus;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ReportRequestRepository<T extends IDownloadableReportRequest> extends JpaRepository<T, Long> {
    List<T> findByRequesterIdAndStatusIn(String requestedId, List<ReportRequestStatus> status);
    List<T> findByStatus(ReportRequestStatus status);
    Optional<T> findByUrlSlug(String urlSlug);
}
