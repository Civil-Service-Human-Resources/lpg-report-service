package uk.gov.cshr.report.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.cshr.report.controller.model.AggregationResponse;
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

    @PostMapping("/aggregations/by-course")
    @ResponseBody
    public AggregationResponse<CourseCompletionAggregation> getCompletionAggregationsByCourse(@RequestBody @Valid GetCourseCompletionsParams params) {
        List<CourseCompletionAggregation> results =  courseCompletionService.getCourseCompletions(params);
        return new AggregationResponse<>(params.getBinDelimiter().getVal(), results);
    }
}
