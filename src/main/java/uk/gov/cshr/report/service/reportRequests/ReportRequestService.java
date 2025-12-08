package uk.gov.cshr.report.service.reportRequests;

import lombok.extern.slf4j.Slf4j;
import uk.gov.cshr.report.config.reports.ReportExportConfig;
import uk.gov.cshr.report.controller.mappers.ReportParamsToRequestMapper;
import uk.gov.cshr.report.controller.model.reportRequest.AddReportRequestResponse;
import uk.gov.cshr.report.controller.model.reportRequest.GetReportRequestParams;
import uk.gov.cshr.report.controller.model.reportRequest.ReportRequestParams;
import uk.gov.cshr.report.domain.report.IDownloadableReportRequest;
import uk.gov.cshr.report.domain.report.ReportRequestStatus;
import uk.gov.cshr.report.repository.ReportRequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ReportRequestService<T extends IDownloadableReportRequest, R extends ReportRequestParams, C extends ReportExportConfig> {
    private final ReportRequestRepository<T> reportRequestRepository;
    private final C config;
    private final ReportParamsToRequestMapper<T, R> paramMapper;

    public ReportRequestService(ReportRequestRepository<T> reportRequestRepository, C config, ReportParamsToRequestMapper<T, R> paramMapper) {
        this.reportRequestRepository = reportRequestRepository;
        this.config = config;
        this.paramMapper = paramMapper;
    }

    public AddReportRequestResponse addReportRequest(R reportRequestParams){
        if(userReachedMaxReportRequests(reportRequestParams.getUserId())){
            return new AddReportRequestResponse(false, "User has reached the maximum allowed report requests");
        }
        T request = paramMapper.buildReportRequest(reportRequestParams);
        reportRequestRepository.save(request);
        return new AddReportRequestResponse(true);
    }

    public List<T> findReportRequestsByUserIdAndStatus(GetReportRequestParams params){
        return reportRequestRepository.findByRequesterIdAndStatusIn(params.getUserId(), params.getStatus().stream().map(ReportRequestStatus::valueOf).collect(Collectors.toList()));
    }

    public boolean userReachedMaxReportRequests(String userId){
        GetReportRequestParams params = new GetReportRequestParams(userId, List.of("REQUESTED"));
        List<T> pendingRequests = findReportRequestsByUserIdAndStatus(params);
        return pendingRequests.size() >= config.getMaxRequestsPerUser();
    }
}
