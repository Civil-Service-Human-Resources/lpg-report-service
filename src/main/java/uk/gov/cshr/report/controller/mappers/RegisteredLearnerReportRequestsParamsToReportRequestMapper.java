package uk.gov.cshr.report.controller.mappers;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.reports.RegisteredLearnerReportConfig;
import uk.gov.cshr.report.controller.model.reportRequest.OrganisationalReportRequestParams;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.util.IUtilService;

import static uk.gov.cshr.report.domain.report.ReportRequestStatus.REQUESTED;

@Component
public class RegisteredLearnerReportRequestsParamsToReportRequestMapper implements ReportParamsToRequestMapper<RegisteredLearnerReportRequest, OrganisationalReportRequestParams> {

    private final IUtilService utilService;
    private final RegisteredLearnerReportConfig config;
    private final IUserAuthService userAuthService;

    public RegisteredLearnerReportRequestsParamsToReportRequestMapper(IUtilService utilService, RegisteredLearnerReportConfig config, IUserAuthService userAuthService) {
        this.utilService = utilService;
        this.config = config;
        this.userAuthService = userAuthService;
    }

    @Override
    public RegisteredLearnerReportRequest buildReportRequest(OrganisationalReportRequestParams params) {
        String timezone = params.getTimezone() == null ? config.getDefaultTimezone() : params.getTimezone();
        boolean hasDetailedExportRole = userAuthService.userHasRole("REGISTERED_LEARNER_REPORT_EXPORT_DETAILED");
        boolean organisationIdsSelected = params.getOrganisationIds() != null;
        boolean detailedExport = hasDetailedExportRole && organisationIdsSelected;
        return new RegisteredLearnerReportRequest(
                params.getUserId(), params.getUserEmail(), utilService.getNow(),
                REQUESTED, params.getFullName(), timezone, utilService.generateRandomString(20),
                params.getDownloadBaseUrl(), params.getOrganisationIds(), detailedExport
        );
    }
}
