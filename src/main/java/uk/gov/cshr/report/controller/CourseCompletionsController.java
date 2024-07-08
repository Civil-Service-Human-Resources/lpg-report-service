package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.mappers.PostCourseCompletionsReportRequestsParamsToReportRequestMapper;
import uk.gov.cshr.report.controller.model.*;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.service.CourseCompletionReportRequestService;
import uk.gov.cshr.report.service.CourseCompletionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/course-completions")
public class CourseCompletionsController {

    private final CourseCompletionService courseCompletionService;
    private final CourseCompletionReportRequestService courseCompletionReportRequestService;
    private final PostCourseCompletionsReportRequestsParamsToReportRequestMapper postCourseCompletionsReportRequestsParamsToReportRequestMapper;

    public CourseCompletionsController(CourseCompletionService courseCompletionService, CourseCompletionReportRequestService courseCompletionReportRequestService) {
        this.courseCompletionService = courseCompletionService;
        this.courseCompletionReportRequestService = courseCompletionReportRequestService;
        this.postCourseCompletionsReportRequestsParamsToReportRequestMapper = new PostCourseCompletionsReportRequestsParamsToReportRequestMapper();
    }

    @PostMapping("/aggregations/by-course")
    @ResponseBody
    public AggregationResponse<CourseCompletionAggregation> getCompletionAggregationsByCourse(@RequestBody @Valid GetCourseCompletionsParams params) {
        List<CourseCompletionAggregation> results =  courseCompletionService.getCourseCompletions(params);
        return new AggregationResponse<>(params.getBinDelimiter().getVal(), results);
    }

    @Transactional
    @PutMapping("/remove-user-details")
    @ResponseBody
    public int removeUserDetails(@RequestBody DeleteUserDetailsParams deleteUserDetailsParams) {
        return courseCompletionService.removeUserDetails(deleteUserDetailsParams.getUids());
    }

    @PostMapping("/report-requests")
    @ResponseBody
    public Map<String, Object> addReportRequest(@RequestBody @Valid PostCourseCompletionsReportRequestParams params){
        Map<String, Object> response = new HashMap<>();

        if(courseCompletionReportRequestService.userReachedMaxReportRequests(params.getUserId())){
            response.put("addedSuccessfully", false);
            response.put("reason", "User has reached the maximum allowed report requests");
            return response;
        }
        CourseCompletionReportRequest reportRequest = postCourseCompletionsReportRequestsParamsToReportRequestMapper.getRequestFromParams(params);
        courseCompletionReportRequestService.addReportRequest(reportRequest);
        response.put("addedSuccessfully", true);
        return response;
    }

    @GetMapping("/report-requests")
    @ResponseBody
    public Map<String, List<CourseCompletionReportRequest>> getAllReportRequests(@RequestBody @Valid GetCourseCompletionsReportRequestParams params){
        List<CourseCompletionReportRequest> reportRequests = courseCompletionReportRequestService.findReportRequestsByUserIdAndStatus(params);
        Map<String, List<CourseCompletionReportRequest>> response = new HashMap<>();
        response.put("requests", reportRequests);
        return response;
    }
}
