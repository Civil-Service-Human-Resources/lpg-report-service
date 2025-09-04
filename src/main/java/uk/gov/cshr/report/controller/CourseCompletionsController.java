package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.model.*;
import uk.gov.cshr.report.controller.model.reportRequest.AddReportRequestResponse;
import uk.gov.cshr.report.controller.model.reportRequest.GetReportRequestParams;
import uk.gov.cshr.report.controller.model.reportRequest.GetReportRequestsResponse;
import uk.gov.cshr.report.controller.model.reportRequest.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.aggregation.Aggregation;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionByOrganisationAggregation;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.service.CourseCompletionReportRequestProcessorService;
import uk.gov.cshr.report.service.CourseCompletionService;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.blob.DownloadableFile;
import uk.gov.cshr.report.service.reportRequests.CourseCompletionReportRequestService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/course-completions")
public class CourseCompletionsController {

    private final CourseCompletionService courseCompletionService;
    private final CourseCompletionReportRequestService courseCompletionReportRequestService;
    private final CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;
    private final IUserAuthService userAuthService;
    private final ControllerUtilities controllerUtilities;

    public CourseCompletionsController(CourseCompletionService courseCompletionService, CourseCompletionReportRequestService courseCompletionReportRequestService,
                                       CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService,
                                       IUserAuthService userAuthService, ControllerUtilities controllerUtilities) {
        this.courseCompletionService = courseCompletionService;
        this.courseCompletionReportRequestService = courseCompletionReportRequestService;
        this.courseCompletionReportRequestProcessorService = courseCompletionReportRequestProcessorService;
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
    public AddReportRequestResponse addReportRequest(@RequestBody @Valid PostCourseCompletionsReportRequestParams params){
        return courseCompletionReportRequestService.addReportRequest(params);
    }

    @GetMapping("/report-requests")
    @ResponseBody
    public GetReportRequestsResponse<CourseCompletionReportRequest> getAllReportRequests(@Valid GetReportRequestParams params){
        List<CourseCompletionReportRequest> reportRequests = courseCompletionReportRequestService.findReportRequestsByUserIdAndStatus(params);
        return new GetReportRequestsResponse<>(reportRequests);
    }

    @GetMapping("/report-requests/downloads/{urlSlug}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadReport(@PathVariable String urlSlug){
        String uid = userAuthService.getUsername();
        DownloadableFile output = courseCompletionReportRequestProcessorService.downloadReport(urlSlug, uid);
        return controllerUtilities.getByteStreamResponse(output.getFileName(), output.getBytes());
    }
}
