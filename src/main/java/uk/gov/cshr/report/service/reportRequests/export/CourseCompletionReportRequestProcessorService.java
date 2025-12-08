package uk.gov.cshr.report.service.reportRequests.export;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.config.reports.CourseCompletionsReportConfig;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.repository.ReportRequestRepository;
import uk.gov.cshr.report.service.NotificationService;
import uk.gov.cshr.report.service.ReportExportZipReportService;
import uk.gov.cshr.report.service.notification.MessageDtoFactory;
import uk.gov.cshr.report.service.reportRequests.IReportRequestService;
import uk.gov.cshr.report.service.util.IUtilService;

@Service
@Slf4j
public class CourseCompletionReportRequestProcessorService extends ReportExportRepositoryService<CourseCompletionEvent, CourseCompletionsReportConfig, CourseCompletionReportRequest> {

    public CourseCompletionReportRequestProcessorService(IUtilService utilService,
                                                         ReportRequestRepository<CourseCompletionReportRequest> reportRequestRepository,
                                                         ReportExportZipReportService reportExportZipReportService,
                                                         CourseCompletionsReportConfig config,
                                                         IReportRequestService<CourseCompletionEvent, CourseCompletionReportRequest> reportRequestService,
                                                         CourseCompletionCsvRowFactory csvRowFactory,
                                                         NotificationService notificationService,
                                                         MessageDtoFactory messageDtoFactory) {
        super(utilService, reportRequestRepository, reportExportZipReportService, config, reportRequestService, csvRowFactory, notificationService, messageDtoFactory);
    }
}
