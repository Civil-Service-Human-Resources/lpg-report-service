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

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class CourseCompletionReportRequestProcessorServiceTest {

    @Mock
    private CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;
    @Mock
    private CourseCompletionService courseCompletionService;
    @Mock
    private CourseCompletionsZipReportService courseCompletionsZipReportService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageDtoFactory messageDtoFactory;

    @TempDir
    File temp;

    @InjectMocks
    private CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;

    @Test
    void testProcessRequestSuccess() {
        CourseCompletionReportRequest request = new CourseCompletionReportRequest(
                "requesterId", "email", ZonedDateTime.now(), ReportRequestStatus.REQUESTED,
                ZonedDateTime.now(), ZonedDateTime.now(), List.of(), List.of(), List.of(), List.of(), "+1", "Name",
                "URL", "http://base.com"
        );
        courseCompletionReportRequestProcessorService.processRequest(temp.toPath(), request);

        verify(messageDtoFactory, never()).getCourseCompletionReportFailureEmail(request);
        assertEquals(ReportRequestStatus.SUCCESS, request.getStatus());
        assertNotNull(request.getCompletedTimestamp());
    }

    @Test
    void testProcessRequestFail() {
        CourseCompletionReportRequest request = new CourseCompletionReportRequest(
                "requesterId", "email", ZonedDateTime.now(), ReportRequestStatus.REQUESTED,
                ZonedDateTime.now(), ZonedDateTime.now(), List.of(), List.of(), List.of(), List.of(), "+1", "Name",
                "URL", "http://base.com"
        );
        when(courseCompletionService.getCourseCompletionEvents(request)).thenThrow(new RuntimeException("Ex"));
        courseCompletionReportRequestProcessorService.processRequest(temp.toPath(), request);

        verify(messageDtoFactory, atMostOnce()).getCourseCompletionReportFailureEmail(request);
        assertEquals(ReportRequestStatus.FAILED, request.getStatus());
        assertNull(request.getCompletedTimestamp());
    }

    @Test
    void downloadReport() {
    }
}
