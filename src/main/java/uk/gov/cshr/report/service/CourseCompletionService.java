package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsByCourseParams;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.aggregation.Aggregation;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionByOrganisationAggregation;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;
import uk.gov.cshr.report.service.reportRequests.IReportRequestService;
import uk.gov.cshr.report.service.util.ITimeUtils;

import java.util.List;

@Service
@Slf4j
public class CourseCompletionService implements IReportRequestService<CourseCompletionEvent, CourseCompletionReportRequest> {

    private final CourseCompletionEventRepository repository;
    private final CourseCompletionsParamsFactory paramsFactory;
    private final ITimeUtils timeUtils;

    public CourseCompletionService(
            CourseCompletionEventRepository repository, CourseCompletionsParamsFactory paramsFactory, ITimeUtils timeUtils) {
        this.repository = repository;
        this.paramsFactory = paramsFactory;
        this.timeUtils = timeUtils;
    }

    public List<CourseCompletionAggregation> getCourseCompletionAggregationsByCourse(GetCourseCompletionsByCourseParams params) {
        return repository.getCompletionsAggregationByCourse(params.getBinDelimiter().getVal(), params.getStartDate(), params.getEndDate(),
                params.getTimezone().toString(), params.getCourseIds(), params.getOrganisationIds(), params.getGradeIds(), params.getProfessionIds());
    }

    public List<CourseCompletionByOrganisationAggregation> getCourseCompletionAggregationsByOrganisation(GetCourseCompletionsByCourseParams params) {
        return repository.getCompletionsAggregationByOrganisation(params.getBinDelimiter().getVal(), params.getStartDate(), params.getEndDate(),
                params.getTimezone().toString(), params.getCourseIds(), params.getOrganisationIds(), params.getGradeIds(), params.getProfessionIds());
    }

    public List<Aggregation> getCourseCompletionAggregations(GetCourseCompletionsParams params) {
        return repository.getCompletionsAggregations(params.getBinDelimiter().getVal(), params.getStartDate(), params.getEndDate(),
                params.getTimezone().toString(), params.getOrganisationIds(), params.getGradeIds(), params.getProfessionIds());
    }

    public List<CourseCompletionEvent> getCourseCompletionEventsWithTimezone(GetCourseCompletionsByCourseParams params) {
        List<CourseCompletionEvent> events = getCourseCompletionEvents(params);
        events.forEach(cce -> cce.setEventTimestamp(timeUtils.convertZonedDateTimeToTimezone(cce.getEventTimestamp(), params.getTimezone().toString())));
        return events;
    }

    public List<CourseCompletionEvent> getCourseCompletionEvents(GetCourseCompletionsByCourseParams params) {
        return repository.getCourseCompletionEvents(
                params.getStartDate(),
                params.getEndDate(),
                params.getCourseIds(),
                params.getOrganisationIds(),
                params.getGradeIds(),
                params.getProfessionIds()
        );
    }

    public int removeUserDetails(List<String> uids) {
        log.info("Removing user details for UIDs: " + uids);
        return repository.removeUserDetails(uids);
    }

    @Override
    public List<CourseCompletionEvent> getReportRequestData(CourseCompletionReportRequest reportRequest) {
        GetCourseCompletionsByCourseParams params = paramsFactory.fromReportRequest(reportRequest);
        return getCourseCompletionEventsWithTimezone(params);
    }
}
