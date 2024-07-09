package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;

import java.util.List;

@Service
@Slf4j
public class CourseCompletionService {

    private final CourseCompletionEventRepository repository;

    public CourseCompletionService(
            CourseCompletionEventRepository repository) {
        this.repository = repository;
    }

    public List<CourseCompletionAggregation> getCourseCompletions(GetCourseCompletionsParams params) {
        return repository.getCompletionsAggregationByCourse(params.getBinDelimiter().getVal(), params.getStartDate(), params.getEndDate(),
                params.getTimezone(), params.getCourseIds(), params.getOrganisationIds(), params.getGradeIds(), params.getProfessionIds());
    }

    @PreAuthorize("hasAnyAuthority('IDENTITY_DELETE')")
    public int removeUserDetails(List<String> uids) {
        log.info("Removing user details for UIDs: " + uids);
        return repository.removeUserDetails(uids);
    }
}
