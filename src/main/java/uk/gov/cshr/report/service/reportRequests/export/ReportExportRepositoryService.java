package uk.gov.cshr.report.service.reportRequests.export;

import lombok.extern.slf4j.Slf4j;
import uk.gov.cshr.report.config.reports.ReportExportConfig;
import uk.gov.cshr.report.domain.report.CsvData;
import uk.gov.cshr.report.domain.report.IDownloadableReportRequest;
import uk.gov.cshr.report.domain.report.ReportRequestStatus;
import uk.gov.cshr.report.domain.report.ReportableData;
import uk.gov.cshr.report.dto.MessageDto;
import uk.gov.cshr.report.exception.ReportNotFoundException;
import uk.gov.cshr.report.exception.UnauthorisedReportDownloadException;
import uk.gov.cshr.report.repository.ReportRequestRepository;
import uk.gov.cshr.report.service.NotificationService;
import uk.gov.cshr.report.service.ReportExportZipReportService;
import uk.gov.cshr.report.service.blob.DownloadableFile;
import uk.gov.cshr.report.service.notification.MessageDtoFactory;
import uk.gov.cshr.report.service.reportRequests.IReportRequestService;
import uk.gov.cshr.report.service.util.IUtilService;
import uk.gov.cshr.report.service.util.TempDirectoryResource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public abstract class ReportExportRepositoryService<T extends ReportableData, C extends ReportExportConfig, R extends IDownloadableReportRequest> {

    private final IUtilService utilService;
    private final ReportRequestRepository<R> reportRequestRepository;
    private final ReportExportZipReportService reportExportZipReportService;
    private final C config;
    private final IReportRequestService<T, R> reportRequestService;
    private final CsvRowFactory<T> csvRowFactory;
    private final NotificationService notificationService;
    private final MessageDtoFactory messageDtoFactory;

    public ReportExportRepositoryService(IUtilService utilService, ReportRequestRepository<R> reportRequestRepository, ReportExportZipReportService reportExportZipReportService, C config,
                                         IReportRequestService<T, R> reportRequestService, CsvRowFactory<T> csvRowFactory, NotificationService notificationService,
                                         MessageDtoFactory messageDtoFactory) {
        this.utilService = utilService;
        this.reportRequestRepository = reportRequestRepository;
        this.reportExportZipReportService = reportExportZipReportService;
        this.config = config;
        this.reportRequestService = reportRequestService;
        this.csvRowFactory = csvRowFactory;
        this.notificationService = notificationService;
        this.messageDtoFactory = messageDtoFactory;
    }

    public void processRequests() throws IOException {
        List<R> requests = reportRequestRepository.findByStatus(ReportRequestStatus.REQUESTED);
        log.info("Found {} requests", requests.size());
        if (!requests.isEmpty()) {
            try (TempDirectoryResource tempDirectory = config.getTempDirectoryResource()) {
                requests.forEach(r -> processRequest(tempDirectory.getFile(), r));
            }
        }
    }

    public void processRequest(Path directoryPath, R request) {
        log.info(String.format("Attempting to Process request %s", request.getReportRequestId()));
        MessageDto message = messageDtoFactory.getReportExportSuccessEmail(request);
        try {
            request.setStatus(ReportRequestStatus.PROCESSING);
            reportRequestRepository.save(request);
            log.debug(String.format("Processing request: %s", request));
            List<T> data = reportRequestService.getReportRequestData(request);
            CsvData<T> csvData = csvRowFactory.getCsvData(data, request.getExportCsvType().getConfig());
            String fileName = String.format("%s/%s", directoryPath, request.getFileName());
            reportExportZipReportService.createAndUploadReport(csvData, config.getBlobContainer(), fileName);
            log.info(String.format("Processing of request with ID %s has succeeded", request.getReportRequestId()));
            request.setStatus(ReportRequestStatus.SUCCESS);
            request.setCompletedTimestamp(utilService.getNow());
        }
        catch (Exception e) {
            log.error(String.format("Error encountered processing request: %s", e));
            request.setStatus(ReportRequestStatus.FAILED);
            message = messageDtoFactory.getReportExportFailureEmail(request);
        }
        reportRequestRepository.save(request);
        notificationService.sendEmail(message);
        log.info(String.format("%s email sent to %s", request.getStatus(), request.getRequesterEmail()));
    }

    public DownloadableFile downloadReport(String urlSlug, String downloaderUid) {
        log.info(String.format("User %s attempting to download report %s", downloaderUid, urlSlug));
        R reportRequest = reportRequestRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ReportNotFoundException(String.format("Course completion report with slug '%s' was not found", urlSlug)));
        if (!reportRequest.getRequesterId().equals(downloaderUid)) {
            throw new UnauthorisedReportDownloadException(String.format("User %s attempted to download course completions report %s",
                    downloaderUid, reportRequest.getRequesterId()));
        }
        reportRequest.setTimesDownloaded(reportRequest.getTimesDownloaded()+1);
        DownloadableFile downloadableFile = reportExportZipReportService.fetchBlobReport(config.getBlobContainer(), reportRequest.getFileName());
        reportRequestRepository.save(reportRequest);
        return downloadableFile;
    }

}
