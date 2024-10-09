package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.CourseCompletionReportRequestStatus;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseCompletionReportRequestService {
    private final CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;
    private int maxRequestsPerUser;

    public CourseCompletionReportRequestService(CourseCompletionReportRequestRepository courseCompletionReportRequestRepository, @Value("${courseCompletions.reports.maxRequestsPerUser}") int maxRequestsPerUser, ReportService reportService) {
        this.courseCompletionReportRequestRepository = courseCompletionReportRequestRepository;
        this.maxRequestsPerUser = maxRequestsPerUser;
    }

    public CourseCompletionReportRequest addReportRequest(CourseCompletionReportRequest reportRequest){
        return courseCompletionReportRequestRepository.save(reportRequest);
    }

    public List<CourseCompletionReportRequest> findReportRequestsByUserIdAndStatus(GetCourseCompletionsReportRequestParams params){
        return courseCompletionReportRequestRepository.findByRequesterIdAndStatusIn(params.getUserId(), params.getStatus());
    }

    public void setStatusForReportRequest(Long reportRequestId, CourseCompletionReportRequestStatus status){
        CourseCompletionReportRequest reportRequest = courseCompletionReportRequestRepository.findByReportRequestId(reportRequestId);
        reportRequest.setStatus(status.toString());
        courseCompletionReportRequestRepository.save(reportRequest);
    }

    public void setCompletedDateForReportRequest(Long reportRequestId, ZonedDateTime completedDate){
        CourseCompletionReportRequest reportRequest = courseCompletionReportRequestRepository.findByReportRequestId(reportRequestId);
        reportRequest.setCompletedTimestamp(completedDate);
        courseCompletionReportRequestRepository.save(reportRequest);
    }

    public List<CourseCompletionReportRequest> findAllRequestsByStatus(CourseCompletionReportRequestStatus status){
        return courseCompletionReportRequestRepository.findByStatus(status.toString());
    }

    public boolean userReachedMaxReportRequests(String userId){
        GetCourseCompletionsReportRequestParams params = new GetCourseCompletionsReportRequestParams(userId, List.of("REQUESTED"));
        List<CourseCompletionReportRequest> pendingRequests = findReportRequestsByUserIdAndStatus(params);
        return pendingRequests.size() >= maxRequestsPerUser;
    }
}
