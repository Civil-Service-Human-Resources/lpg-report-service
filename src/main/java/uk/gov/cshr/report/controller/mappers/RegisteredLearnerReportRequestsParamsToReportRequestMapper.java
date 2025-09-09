package uk.gov.cshr.report.controller.mappers;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.controller.model.reportRequest.OrganisationalReportRequestParams;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.service.util.IUtilService;

import static uk.gov.cshr.report.domain.report.ReportRequestStatus.REQUESTED;

@Component
public class RegisteredLearnerReportRequestsParamsToReportRequestMapper implements ReportParamsToRequestMapper<RegisteredLearnerReportRequest, OrganisationalReportRequestParams> {

    private final IUtilService utilService;

    public RegisteredLearnerReportRequestsParamsToReportRequestMapper(IUtilService utilService) {
        this.utilService = utilService;
    }

    @Override
    public RegisteredLearnerReportRequest buildReportRequest(OrganisationalReportRequestParams params) {
        return new RegisteredLearnerReportRequest(
                params.getUserId(), params.getUserEmail(), utilService.getNow(),
                REQUESTED, params.getFullName(), utilService.generateRandomString(20), params.getDownloadBaseUrl(), params.getOrganisationIds()
        );
    }
}
