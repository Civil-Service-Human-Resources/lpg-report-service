package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsByCourseParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;

@Service
public class CourseCompletionsParamsFactory {

    public GetCourseCompletionsByCourseParams fromReportRequest(CourseCompletionReportRequest request){
        GetCourseCompletionsByCourseParams params = new GetCourseCompletionsByCourseParams();
        params.setCourseIds(request.getCourseIds());
        params.setOrganisationIds(request.getOrganisationIds());
        params.setStartDate(request.getFromDate().toLocalDateTime());
        params.setEndDate(request.getToDate().toLocalDateTime());
        return params;
    }

}
