package uk.gov.cshr.report.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.report.ReportRequestStatus;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;
import uk.gov.cshr.report.service.notification.MessageDtoFactory;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionCsvRowFactory;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionReportRequestProcessorService;
import uk.gov.cshr.report.service.util.IUtilService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class CourseCompletionReportRequestProcessorServiceTest {

    @Mock
    private CourseCompletionService courseCompletionService;
    @Mock
    private CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;
    @Mock
    private CourseCompletionCsvRowFactory csvRowFactory;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ReportExportZipReportService reportExportZipReportService;
    @Mock
    private MessageDtoFactory messageDtoFactory;
    @Mock
    private IUtilService utilService;

    @TempDir
    File temp;

    @InjectMocks
    private CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;

    @Test
    void testProcessRequestSuccess() {
        LocalDateTime now = LocalDateTime.now();
        CourseCompletionReportRequest request = new CourseCompletionReportRequest(
                "requesterId", "email", now, ReportRequestStatus.REQUESTED,
                now, now, List.of(), List.of(), List.of(), List.of(), "+1", "Name",
                "URL", "http://base.com"
        );
        when(utilService.getNow()).thenReturn(now);
        courseCompletionReportRequestProcessorService.processRequest(temp.toPath(), request);
        verify(messageDtoFactory, never()).getReportExportFailureEmail(request);
        assertEquals(ReportRequestStatus.SUCCESS, request.getStatus());
        assertNotNull(request.getCompletedTimestamp());
    }

    @Test
    void testProcessRequestFail() {
        CourseCompletionReportRequest request = new CourseCompletionReportRequest(
                "requesterId", "email", LocalDateTime.now(), ReportRequestStatus.REQUESTED,
                LocalDateTime.now(), LocalDateTime.now(), List.of(), List.of(), List.of(), List.of(), "+1", "Name",
                "URL", "http://base.com"
        );
        when(courseCompletionService.getReportRequestData(request)).thenThrow(new RuntimeException("Ex"));
        courseCompletionReportRequestProcessorService.processRequest(temp.toPath(), request);

        verify(messageDtoFactory, atMostOnce()).getReportExportFailureEmail(request);
        assertEquals(ReportRequestStatus.FAILED, request.getStatus());
        assertNull(request.getCompletedTimestamp());
    }

    @Test
    void downloadReport() {
    }
}
