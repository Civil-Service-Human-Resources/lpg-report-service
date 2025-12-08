package uk.gov.cshr.report.service.reportRequests.export;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.config.reports.RegisteredLearnerReportConfig;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.repository.ReportRequestRepository;
import uk.gov.cshr.report.service.NotificationService;
import uk.gov.cshr.report.service.ReportExportZipReportService;
import uk.gov.cshr.report.service.notification.MessageDtoFactory;
import uk.gov.cshr.report.service.reportRequests.IReportRequestService;
import uk.gov.cshr.report.service.util.IUtilService;

@Service
@Slf4j
public class RegisteredLearnersReportRequestProcessorService extends ReportExportRepositoryService<RegisteredLearner, RegisteredLearnerReportConfig, RegisteredLearnerReportRequest> {

    public RegisteredLearnersReportRequestProcessorService(IUtilService utilService,
                                                           ReportRequestRepository<RegisteredLearnerReportRequest> reportRequestRepository,
                                                           ReportExportZipReportService reportExportZipReportService,
                                                           RegisteredLearnerReportConfig config,
                                                           IReportRequestService<RegisteredLearner, RegisteredLearnerReportRequest> reportRequestService,
                                                           CsvRowFactory<RegisteredLearner> csvRowFactory,
                                                           NotificationService notificationService, MessageDtoFactory messageDtoFactory) {
        super(utilService, reportRequestRepository, reportExportZipReportService, config, reportRequestService, csvRowFactory, notificationService, messageDtoFactory);
    }
}
