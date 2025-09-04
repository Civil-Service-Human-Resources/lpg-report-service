package uk.gov.cshr.report.controller.mappers;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.reports.CourseCompletionsReportConfig;
import uk.gov.cshr.report.controller.model.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.util.StringUtils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static uk.gov.cshr.report.domain.report.ReportRequestStatus.REQUESTED;

@Component
public class PostCourseCompletionsReportRequestsParamsToReportRequestMapper implements ReportParamsToRequestMapper<CourseCompletionReportRequest, PostCourseCompletionsReportRequestParams> {

    private final StringUtils stringUtils;
    private final CourseCompletionsReportConfig config;
    private final IUserAuthService userAuthService;

    public PostCourseCompletionsReportRequestsParamsToReportRequestMapper(StringUtils stringUtils,
                                                                          CourseCompletionsReportConfig config,
                                                                          IUserAuthService userAuthService) {
        this.stringUtils = stringUtils;
        this.config = config;
        this.userAuthService = userAuthService;
    }

    @Override
    public CourseCompletionReportRequest buildReportRequest(PostCourseCompletionsReportRequestParams params) {
        String timezone = params.getTimezone() == null ? config.getDefaultTimezone() : params.getTimezone();
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
