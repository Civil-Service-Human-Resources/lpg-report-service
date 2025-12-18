package uk.gov.cshr.report.controller.mappers;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.reports.CourseCompletionsReportConfig;
import uk.gov.cshr.report.controller.model.reportRequest.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.util.IUtilService;

import static uk.gov.cshr.report.domain.report.ReportRequestStatus.REQUESTED;

@Component
public class PostCourseCompletionsReportRequestsParamsToReportRequestMapper implements ReportParamsToRequestMapper<CourseCompletionReportRequest, PostCourseCompletionsReportRequestParams> {

    private final IUtilService utilService;
    private final CourseCompletionsReportConfig config;
    private final IUserAuthService userAuthService;

    public PostCourseCompletionsReportRequestsParamsToReportRequestMapper(IUtilService utilService,
                                                                          CourseCompletionsReportConfig config,
                                                                          IUserAuthService userAuthService) {
        this.utilService = utilService;
        this.config = config;
        this.userAuthService = userAuthService;
    }

    @Override
    public CourseCompletionReportRequest buildReportRequest(PostCourseCompletionsReportRequestParams params) {
        String timezone = params.getTimezone() == null ? config.getDefaultTimezone() : params.getTimezone();
        String slug = utilService.generateRandomString(20);

        CourseCompletionReportRequest courseCompletionReportRequest = new CourseCompletionReportRequest(
                params.getUserId(), params.getUserEmail(), utilService.getNow(),
                REQUESTED, params.getStartDate(), params.getEndDate(),
                params.getCourseIds(), params.getOrganisationIds(), params.getProfessionIds(), params.getGradeIds(),
                timezone, params.getFullName(), slug, params.getDownloadBaseUrl()
        );

        Boolean hasDetailedExportRole = userAuthService.userHasRole("REPORT_EXPORT_DETAILED");
        Boolean organisationIdsSelected = courseCompletionReportRequest.getOrganisationIds() != null && !courseCompletionReportRequest.getOrganisationIds().isEmpty();
        Boolean userCanHaveDetailedExport = hasDetailedExportRole && organisationIdsSelected;
        courseCompletionReportRequest.setDetailedExport(userCanHaveDetailedExport);
        return courseCompletionReportRequest;
    }
}
