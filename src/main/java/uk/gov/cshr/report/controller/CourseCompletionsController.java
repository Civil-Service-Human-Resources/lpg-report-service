package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.mappers.PostCourseCompletionsReportRequestsParamsToReportRequestMapper;
import uk.gov.cshr.report.controller.model.*;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.service.CourseCompletionReportRequestService;
import uk.gov.cshr.report.service.CourseCompletionService;

import java.util.List;

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
        return new AggregationResponse<>(params.getTimezone().toString(), params.getBinDelimiter().getVal(), results);
    }

    @Transactional
    @PutMapping("/remove-user-details")
    @ResponseBody
    public int removeUserDetails(@RequestBody DeleteUserDetailsParams deleteUserDetailsParams) {
        return courseCompletionService.removeUserDetails(deleteUserDetailsParams.getUids());
    }

    @PostMapping("/report-requests")
    @ResponseBody
    public AddCourseCompletionReportRequestResponse addReportRequest(@RequestBody @Valid PostCourseCompletionsReportRequestParams params){
        if(courseCompletionReportRequestService.userReachedMaxReportRequests(params.getUserId())){
            return new AddCourseCompletionReportRequestResponse(false, "User has reached the maximum allowed report requests");
        }
        CourseCompletionReportRequest reportRequest = postCourseCompletionsReportRequestsParamsToReportRequestMapper.getRequestFromParams(params);
        courseCompletionReportRequestService.addReportRequest(reportRequest);
        return new AddCourseCompletionReportRequestResponse(true);
    }

    @GetMapping("/report-requests")
    @ResponseBody
    public GetCourseCompletionReportRequestsResponse getAllReportRequests(@RequestBody @Valid GetCourseCompletionsReportRequestParams params){
        List<CourseCompletionReportRequest> reportRequests = courseCompletionReportRequestService.findReportRequestsByUserIdAndStatus(params);
        return new GetCourseCompletionReportRequestsResponse(reportRequests);
    }
}
