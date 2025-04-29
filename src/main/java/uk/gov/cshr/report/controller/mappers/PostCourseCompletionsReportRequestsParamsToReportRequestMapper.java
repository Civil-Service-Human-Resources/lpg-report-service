package uk.gov.cshr.report.controller.mappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.controller.model.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.service.CourseCompletionReportRequestService;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.util.StringUtils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static uk.gov.cshr.report.domain.report.CourseCompletionReportRequestStatus.REQUESTED;

@Component
public class PostCourseCompletionsReportRequestsParamsToReportRequestMapper {

    private final StringUtils stringUtils;
    private final String defaultTimezone;
    private final IUserAuthService userAuthService;

    public PostCourseCompletionsReportRequestsParamsToReportRequestMapper(StringUtils stringUtils,
                                                                          @Value("${courseCompletions.reports.defaultTimezone}") String defaultTimezone,
                                                                          IUserAuthService userAuthService, CourseCompletionReportRequestService courseCompletionReportRequestService) {
        this.stringUtils = stringUtils;
        this.defaultTimezone = defaultTimezone;
        this.userAuthService = userAuthService;
    }

    public CourseCompletionReportRequest getRequestFromParams(PostCourseCompletionsReportRequestParams params) {
        String timezone = params.getTimezone() == null ? defaultTimezone : params.getTimezone();
        String slug = stringUtils.generateRandomString(20);

        CourseCompletionReportRequest courseCompletionReportRequest = new CourseCompletionReportRequest(
                params.getUserId(), params.getUserEmail(), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                REQUESTED, params.getStartDate().atZone(ZoneOffset.UTC), params.getEndDate().atZone(ZoneOffset.UTC),
                params.getCourseIds(), params.getOrganisationIds(), params.getProfessionIds(), params.getGradeIds(),
                timezone, params.getFullName(), slug, params.getDownloadBaseUrl()
        );

        Boolean hasDetailedExportRole = userAuthService.userHasRole("REPORT_EXPORT_DETAILED");
        Boolean organisationIdsSelected = courseCompletionReportRequest.getOrganisationIds() != null;
        Boolean userCanHaveDetailedExport = hasDetailedExportRole && organisationIdsSelected;
        courseCompletionReportRequest.setDetailedExport(userCanHaveDetailedExport);
        return courseCompletionReportRequest;
    }
}
