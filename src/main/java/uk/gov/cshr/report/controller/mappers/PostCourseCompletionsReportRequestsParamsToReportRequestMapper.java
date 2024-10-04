package uk.gov.cshr.report.controller.mappers;

import uk.gov.cshr.report.controller.model.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class PostCourseCompletionsReportRequestsParamsToReportRequestMapper {
    public CourseCompletionReportRequest getRequestFromParams(PostCourseCompletionsReportRequestParams params) {
        CourseCompletionReportRequest reportRequest = new CourseCompletionReportRequest();
        reportRequest.setRequesterId(params.getUserId());
        reportRequest.setRequesterEmail(params.getUserEmail());
        reportRequest.setStatus("REQUESTED");
        reportRequest.setRequestedTimestamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        reportRequest.setFromDate(params.getStartDate().atZone(ZoneOffset.UTC));
        reportRequest.setToDate(params.getEndDate().atZone(ZoneOffset.UTC));
        reportRequest.setCourseIds(params.getCourseIds());
        reportRequest.setOrganisationIds(params.getOrganisationIds());
        reportRequest.setProfessionIds(params.getProfessionIds());
        reportRequest.setGradeIds(params.getGradeIds());
        reportRequest.setRequesterTimezone(params.getTimezone());

        return reportRequest;
    }
}
