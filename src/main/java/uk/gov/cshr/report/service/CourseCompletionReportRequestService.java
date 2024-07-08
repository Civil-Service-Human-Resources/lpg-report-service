package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;

import java.util.List;

@Service
@Slf4j
public class CourseCompletionReportRequestService {
    private final CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;
    private int maxRequestsPerUser;

    public CourseCompletionReportRequestService(CourseCompletionReportRequestRepository courseCompletionReportRequestRepository, @Value("${courseCompletions.reports.maxRequestsPerUser}") int maxRequestsPerUser) {
        this.courseCompletionReportRequestRepository = courseCompletionReportRequestRepository;
        this.maxRequestsPerUser = maxRequestsPerUser;
    }

    public CourseCompletionReportRequest addReportRequest(CourseCompletionReportRequest reportRequest){
        return courseCompletionReportRequestRepository.save(reportRequest);
    }

    public List<CourseCompletionReportRequest> findReportRequestsByUserIdAndStatus(GetCourseCompletionsReportRequestParams params){
        return courseCompletionReportRequestRepository.findByRequesterIdAndStatus(params.getUserId(), params.getStatus());
    }

    public boolean userReachedMaxReportRequests(String userId){
        GetCourseCompletionsReportRequestParams params = new GetCourseCompletionsReportRequestParams(userId, "REQUESTED");
        List<CourseCompletionReportRequest> pendingRequests = findReportRequestsByUserIdAndStatus(params);
        return pendingRequests.size() >= maxRequestsPerUser;
    }
}
