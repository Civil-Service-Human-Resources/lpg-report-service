package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;

import java.util.List;

@Service
@Slf4j
public class CourseCompletionService {

    private final CourseCompletionEventRepository repository;
    private final CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;

    public CourseCompletionService(CourseCompletionEventRepository repository, CourseCompletionReportRequestRepository courseCompletionReportRequestRepository) {
        this.repository = repository;
        this.courseCompletionReportRequestRepository = courseCompletionReportRequestRepository;
    }

    public List<CourseCompletionAggregation> getCourseCompletions(GetCourseCompletionsParams params) {
        return repository.getCompletionsAggregationByCourse(params.getBinDelimiter().getVal(), params.getStartDateTime(), params.getEndDateTime(),
                params.getCourseIds(), params.getOrganisationIds(), params.getGradeIds(), params.getProfessionIds());
    }

    @PreAuthorize("hasAnyAuthority('IDENTITY_DELETE')")
    public int removeUserDetails(List<String> uids) {
        log.info("Removing user details for UIDs: " + uids);
        return repository.removeUserDetails(uids);
    }

    public CourseCompletionReportRequest addReportRequest(CourseCompletionReportRequest reportRequest){
        return courseCompletionReportRequestRepository.save(reportRequest);
    }

    public List<CourseCompletionReportRequest> findAllReportRequests(){
        return courseCompletionReportRequestRepository.findAll();
    }
}
