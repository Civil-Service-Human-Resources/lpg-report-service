package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;

import java.util.List;

@Service
public class CourseCompletionService {

    private final CourseCompletionEventRepository repository;

    public CourseCompletionService(CourseCompletionEventRepository repository) {
        this.repository = repository;
    }

    public List<CourseCompletionAggregation> getCourseCompletions(GetCourseCompletionsParams params) {
        return repository.getCompletionsAggregationByCourse(params.getBinDelimiter().getVal(), params.getStartDate(), params.getEndDate(),
                params.getCourseIds(), params.getOrganisationIds(), params.getGradeIds(), params.getProfessionIds());
    }
}
