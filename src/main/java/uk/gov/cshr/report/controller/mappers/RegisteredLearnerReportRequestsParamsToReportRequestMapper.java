package uk.gov.cshr.report.controller.mappers;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.controller.model.reportRequest.OrganisationalReportRequestParams;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.service.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static uk.gov.cshr.report.domain.report.ReportRequestStatus.REQUESTED;

@Component
public class RegisteredLearnerReportRequestsParamsToReportRequestMapper implements ReportParamsToRequestMapper<RegisteredLearnerReportRequest, OrganisationalReportRequestParams> {

    private final StringUtils stringUtils;
    public RegisteredLearnerReportRequestsParamsToReportRequestMapper(StringUtils stringUtils) {
        this.stringUtils = stringUtils;
    }

    @Override
    public RegisteredLearnerReportRequest buildReportRequest(OrganisationalReportRequestParams params) {
        return new RegisteredLearnerReportRequest(
                params.getUserId(), params.getUserEmail(), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                REQUESTED, params.getFullName(), stringUtils.generateRandomString(20), params.getDownloadBaseUrl(), params.getOrganisationIds()
        );
    }
}
