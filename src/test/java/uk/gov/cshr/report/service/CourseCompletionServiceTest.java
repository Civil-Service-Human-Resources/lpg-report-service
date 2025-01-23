package uk.gov.cshr.report.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequestStatus;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CourseCompletionServiceTest {

    @Autowired
    private CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;

    @Autowired
    private CourseCompletionService courseCompletionService;

    @BeforeEach
    public void before() {
        courseCompletionReportRequestRepository.deleteAll();
    }

    @Test
    public void testGetCourseEventsWithRequest() {
        CourseCompletionReportRequest request = new CourseCompletionReportRequest();
        request.setRequestedTimestamp(LocalDateTime.of(2024, 1, 1, 0, 0).atZone(ZoneId.of("UTC")));
        request.setFromDate(LocalDateTime.of(2024, 1, 1, 0, 0).atZone(ZoneId.of("UTC")));
        request.setToDate(LocalDateTime.of(2024, 1, 1, 23, 59).atZone(ZoneId.of("UTC")));
        request.setUrlSlug("test-service-slug");
        request.setDownloadBaseUrl("http://base.com");
        request.setStatus(CourseCompletionReportRequestStatus.REQUESTED);
        request.setCourseIds(List.of());
        request.setOrganisationIds(List.of(1));
        request.setGradeIds(null);
        request.setProfessionIds(null);
        courseCompletionReportRequestRepository.save(request);
        List<CourseCompletionEvent> events = courseCompletionService.getCourseCompletionEvents(request);
        assertEquals(9, events.size());
    }
}
