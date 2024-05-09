package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.model.AggregationResponse;
import uk.gov.cshr.report.controller.model.DeleteUserDetailsParams;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.service.CourseCompletionService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/course-completions")
public class CourseCompletionsController {

    private final CourseCompletionService courseCompletionService;

    public CourseCompletionsController(CourseCompletionService courseCompletionService) {
        this.courseCompletionService = courseCompletionService;
    }

    @GetMapping("/aggregations/by-course")
    @ResponseBody
    public AggregationResponse<CourseCompletionAggregation> getCompletionAggregationsByCourse(@Valid GetCourseCompletionsParams params) {
        List<CourseCompletionAggregation> results =  courseCompletionService.getCourseCompletions(params);
        return new AggregationResponse<>(params.getBinDelimiter().getVal(), results);
    }

    @Transactional
    @PutMapping("/remove-user-details")
    @ResponseBody
    public int removeUserDetails(@RequestBody DeleteUserDetailsParams deleteUserDetailsParams) {
        return courseCompletionService.removeUserDetails(deleteUserDetailsParams.getUids());
    }
}
