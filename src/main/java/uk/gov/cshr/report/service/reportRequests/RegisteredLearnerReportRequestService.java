package uk.gov.cshr.report.service.reportRequests;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.config.reports.RegisteredLearnerReportConfig;
import uk.gov.cshr.report.config.reports.ReportExportConfig;
import uk.gov.cshr.report.controller.mappers.RegisteredLearnerReportRequestsParamsToReportRequestMapper;
import uk.gov.cshr.report.controller.model.reportRequest.OrganisationalReportRequestParams;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.repository.ReportRequestRepository;

@Service
public class RegisteredLearnerReportRequestService extends ReportRequestService<RegisteredLearnerReportRequest, OrganisationalReportRequestParams, ReportExportConfig> {
    public RegisteredLearnerReportRequestService(ReportRequestRepository<RegisteredLearnerReportRequest> reportRequestRepository,
                                                 RegisteredLearnerReportConfig config,
                                                 RegisteredLearnerReportRequestsParamsToReportRequestMapper mapper) {
        super(reportRequestRepository, config, mapper);
    }
}
