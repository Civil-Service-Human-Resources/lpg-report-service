package uk.gov.cshr.report.service.reportRequests;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.config.reports.CourseCompletionsReportConfig;
import uk.gov.cshr.report.controller.mappers.PostCourseCompletionsReportRequestsParamsToReportRequestMapper;
import uk.gov.cshr.report.controller.model.reportRequest.PostCourseCompletionsReportRequestParams;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.repository.ReportRequestRepository;

@Service
public class CourseCompletionReportRequestService extends ReportRequestService<CourseCompletionReportRequest, PostCourseCompletionsReportRequestParams, CourseCompletionsReportConfig> {
    public CourseCompletionReportRequestService(ReportRequestRepository<CourseCompletionReportRequest> reportRequestRepository,
                                                CourseCompletionsReportConfig config,
                                                PostCourseCompletionsReportRequestsParamsToReportRequestMapper mapper) {
        super(reportRequestRepository, config, mapper);
    }
}
