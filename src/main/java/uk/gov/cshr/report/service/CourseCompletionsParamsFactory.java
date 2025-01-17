package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;

@Service
public class CourseCompletionsParamsFactory {

    public GetCourseCompletionsParams fromReportRequest(CourseCompletionReportRequest request){
        GetCourseCompletionsParams params = new GetCourseCompletionsParams();
        params.setCourseIds(request.getCourseIds());
        params.setOrganisationIds(request.getOrganisationIds());
        params.setStartDate(request.getFromDate().toLocalDateTime());
        params.setEndDate(request.getToDate().toLocalDateTime());
        return params;
    }

}
