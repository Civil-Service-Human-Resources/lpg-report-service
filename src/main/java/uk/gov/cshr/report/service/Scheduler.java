package uk.gov.cshr.report.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionReportRequestProcessorService;

import java.io.IOException;

@Component
@Slf4j
@Setter
public class Scheduler {

    private final CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;

    public Scheduler(CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService) {
        this.courseCompletionReportRequestProcessorService = courseCompletionReportRequestProcessorService;
    }

    @Scheduled(cron = "${report-export.courseCompletions.jobCron}")
    @SchedulerLock(name = "reportRequestsJob", lockAtMostFor = "PT4H")
    public void generateReportsForCourseCompletionRequests() throws IOException {
        LockAssert.assertLocked();
        log.info("Starting job for course completion report requests");
        courseCompletionReportRequestProcessorService.processRequests();
        log.info("Finished job for course completion report requests");
    }
}
