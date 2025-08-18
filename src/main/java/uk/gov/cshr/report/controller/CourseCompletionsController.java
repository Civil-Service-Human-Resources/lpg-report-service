package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.mappers.PostCourseCompletionsReportRequestsParamsToReportRequestMapper;
import uk.gov.cshr.report.controller.model.*;
import uk.gov.cshr.report.domain.aggregation.Aggregation;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionByOrganisationAggregation;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.service.CourseCompletionReportRequestProcessorService;
import uk.gov.cshr.report.service.CourseCompletionReportRequestService;
import uk.gov.cshr.report.service.CourseCompletionService;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.blob.DownloadableFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/course-completions")
public class CourseCompletionsController {

    private final CourseCompletionService courseCompletionService;
    private final CourseCompletionReportRequestService courseCompletionReportRequestService;
    private final CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;
    private final PostCourseCompletionsReportRequestsParamsToReportRequestMapper postCourseCompletionsReportRequestsParamsToReportRequestMapper;
    private final IUserAuthService userAuthService;
    private final ControllerUtilities controllerUtilities;

    public CourseCompletionsController(CourseCompletionService courseCompletionService, CourseCompletionReportRequestService courseCompletionReportRequestService,
                                       CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService, PostCourseCompletionsReportRequestsParamsToReportRequestMapper postCourseCompletionsReportRequestsParamsToReportRequestMapper, IUserAuthService userAuthService, ControllerUtilities controllerUtilities) {
        this.courseCompletionService = courseCompletionService;
        this.courseCompletionReportRequestService = courseCompletionReportRequestService;
        this.courseCompletionReportRequestProcessorService = courseCompletionReportRequestProcessorService;
        this.postCourseCompletionsReportRequestsParamsToReportRequestMapper = postCourseCompletionsReportRequestsParamsToReportRequestMapper;
        this.userAuthService = userAuthService;
        this.controllerUtilities = controllerUtilities;
    }

    @PostMapping("/aggregations/by-organisation")
    @ResponseBody
    public AggregationResponse<CourseCompletionByOrganisationAggregation> getCompletionAggregationsByOrganisation(@RequestBody @Valid GetCourseCompletionsByCourseParams params) {
        List<CourseCompletionByOrganisationAggregation> results =  courseCompletionService.getCourseCompletionAggregationsByOrganisation(params);
        return new AggregationResponse<>(params.getTimezone().toString(), params.getBinDelimiter().getVal(), results);
    }

    @PostMapping("/aggregations/by-course")
    @ResponseBody
    public AggregationResponse<CourseCompletionAggregation> getCompletionAggregationsByCourse(@RequestBody @Valid GetCourseCompletionsByCourseParams params) {
        List<CourseCompletionAggregation> results =  courseCompletionService.getCourseCompletionAggregationsByCourse(params);
        return new AggregationResponse<>(params.getTimezone().toString(), params.getBinDelimiter().getVal(), results);
    }

    @PostMapping("/aggregations")
    @ResponseBody
    public AggregationResponse<Aggregation> getCompletionAggregations(@RequestBody @Valid GetCourseCompletionsParams params) {
        List<Aggregation> results =  courseCompletionService.getCourseCompletionAggregations(params);
        return new AggregationResponse<>(params.getTimezone().toString(), params.getBinDelimiter().getVal(), results);
    }

    @Transactional
    @PutMapping("/remove-user-details")
    @ResponseBody
    public UpdateUserResult removeUserDetails(@RequestBody UpdateUserDetailsParams deleteUserDetailsParams) {
        return new UpdateUserResult(courseCompletionService.removeUserDetails(deleteUserDetailsParams.getUids()));
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
    public GetCourseCompletionReportRequestsResponse getAllReportRequests(@Valid GetCourseCompletionsReportRequestParams params){
        List<CourseCompletionReportRequest> reportRequests = courseCompletionReportRequestService.findReportRequestsByUserIdAndStatus(params);
        return new GetCourseCompletionReportRequestsResponse(reportRequests);
    }

    @GetMapping("/report-requests/downloads/{urlSlug}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadReport(@PathVariable String urlSlug){
        String uid = userAuthService.getUsername();
        DownloadableFile output = courseCompletionReportRequestProcessorService.downloadReport(urlSlug, uid);
        return controllerUtilities.getByteStreamResponse(output.getFileName(), output.getBytes());
    }
}
