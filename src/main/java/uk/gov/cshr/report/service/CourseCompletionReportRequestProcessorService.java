package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequestStatus;
import uk.gov.cshr.report.dto.MessageDto;
import uk.gov.cshr.report.exception.ReportNotFoundException;
import uk.gov.cshr.report.exception.UnauthorisedReportDownloadException;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;
import uk.gov.cshr.report.service.blob.DownloadableFile;
import uk.gov.cshr.report.service.notification.MessageDtoFactory;
import uk.gov.cshr.report.service.util.TempDirectoryResource;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseCompletionReportRequestProcessorService {
    private final CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;
    private final CourseCompletionService courseCompletionService;
    private final CourseCompletionsZipReportService courseCompletionsZipReportService;
    private final NotificationService notificationService;
    private final MessageDtoFactory messageDtoFactory;
    private final String tempDir;

    public CourseCompletionReportRequestProcessorService(CourseCompletionReportRequestRepository courseCompletionReportRequestRepository,
                                                         CourseCompletionService courseCompletionService,
                                                         CourseCompletionsZipReportService courseCompletionsZipReportService,
                                                         NotificationService notificationService, MessageDtoFactory messageDtoFactory,
                                                         @Value("${courseCompletions.reports.tempDir}") String tempDir) {
        this.courseCompletionReportRequestRepository = courseCompletionReportRequestRepository;
        this.courseCompletionService = courseCompletionService;
        this.courseCompletionsZipReportService = courseCompletionsZipReportService;
        this.notificationService = notificationService;
        this.messageDtoFactory = messageDtoFactory;
        this.tempDir = tempDir;
    }

    public void processRequests() throws IOException {
        List<CourseCompletionReportRequest> requests = courseCompletionReportRequestRepository.findByStatus(CourseCompletionReportRequestStatus.REQUESTED);
        log.info(String.format("Found %d requests", requests.size()));
        if (!requests.isEmpty()) {
            try (TempDirectoryResource tempDirectory = new TempDirectoryResource(tempDir)) {
                requests.forEach(r -> processRequest(tempDirectory.getFile(), r));
            }
        }

    }

    public void processRequest(Path directoryPath, CourseCompletionReportRequest request) {
        log.info(String.format("Attempting to Process request %s", request.getReportRequestId()));
        MessageDto message = messageDtoFactory.getCourseCompletionReportSuccessEmail(request);
        try {
            request.setStatus(CourseCompletionReportRequestStatus.PROCESSING);
            courseCompletionReportRequestRepository.save(request);
            log.debug(String.format("Processing request: %s", request));
            List<CourseCompletionEvent> courseCompletions = courseCompletionService.getCourseCompletionEvents(request);
            String fileName = String.format("%s/%s", directoryPath, request.getFileName());
            courseCompletionsZipReportService.createAndUploadReport(courseCompletions, fileName);
            log.info(String.format("Processing of request with ID %s has succeeded", request.getReportRequestId()));
            request.setStatus(CourseCompletionReportRequestStatus.SUCCESS);
            ZonedDateTime completedDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
            request.setCompletedTimestamp(completedDate);
        }
        catch (Exception e) {
            log.error(String.format("Error encountered processing request: %s", e));
            request.setStatus(CourseCompletionReportRequestStatus.FAILED);
            message = messageDtoFactory.getCourseCompletionReportFailureEmail(request);
        }
        courseCompletionReportRequestRepository.save(request);
        notificationService.sendEmail(message);
        log.info(String.format("%s email sent to %s", request.getStatus(), request.getRequesterEmail()));
    }

    public DownloadableFile downloadReport(String urlSlug, String downloaderUid) {
        log.info(String.format("User %s attempting to download report %s", downloaderUid, urlSlug));
        CourseCompletionReportRequest courseCompletionReportRequest = courseCompletionReportRequestRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ReportNotFoundException(String.format("Course completion report with slug '%s' was not found", urlSlug)));
        if (!courseCompletionReportRequest.getRequesterId().equals(downloaderUid)) {
            throw new UnauthorisedReportDownloadException(String.format("User %s attempted to download course completions report %s",
                    downloaderUid, courseCompletionReportRequest.getRequesterId()));
        }
        courseCompletionReportRequest.setDownloadMetadata();
        return courseCompletionsZipReportService.fetchBlobReport(courseCompletionReportRequest.getFileName());
    }

}
