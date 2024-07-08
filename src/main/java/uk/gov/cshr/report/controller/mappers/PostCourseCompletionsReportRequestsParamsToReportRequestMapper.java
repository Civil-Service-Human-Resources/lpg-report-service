package uk.gov.cshr.report.controller.mappers;

import uk.gov.cshr.report.controller.model.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class PostCourseCompletionsReportRequestsParamsToReportRequestMapper {
    public CourseCompletionReportRequest getRequestFromParams(PostCourseCompletionsReportRequestParams params) {
        CourseCompletionReportRequest reportRequest = new CourseCompletionReportRequest();
        reportRequest.setRequesterId(params.getUserId());
        reportRequest.setRequesterEmail(params.getUserEmail());
        reportRequest.setStatus("REQUESTED");
        reportRequest.setRequestedTimestamp(ZonedDateTime.now());
        reportRequest.setFromDate(params.getStartDate().atStartOfDay(ZoneOffset.UTC));
        reportRequest.setToDate(params.getEndDate().atStartOfDay(ZoneOffset.UTC));
        reportRequest.setCourseIds(params.getCourseIds());
        reportRequest.setOrganisationIds(params.getOrganisationIds());
        reportRequest.setProfessionIds(params.getProfessionIds());
        reportRequest.setGradeIds(params.getGradeIds());

        return reportRequest;
    }
}
