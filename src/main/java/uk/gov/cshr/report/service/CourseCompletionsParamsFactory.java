package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsByCourseParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class CourseCompletionsParamsFactory {

    public GetCourseCompletionsByCourseParams fromReportRequest(CourseCompletionReportRequest request){
        GetCourseCompletionsByCourseParams params = new GetCourseCompletionsByCourseParams();
        List<String> courseIds = request.getCourseIds();
        params.setCourseIds(isEmpty(courseIds) ? null : courseIds);
        params.setOrganisationIds(request.getOrganisationIds());
        params.setStartDate(request.getFromDate().toLocalDateTime());
        params.setEndDate(request.getToDate().toLocalDateTime());
        return params;
    }

}
