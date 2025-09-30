package uk.gov.cshr.report.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionReportRequestProcessorService;
import uk.gov.cshr.report.service.reportRequests.export.RegisteredLearnersReportRequestProcessorService;

import java.io.IOException;

@Component
@Slf4j
@Setter
public class Scheduler {

    private final CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;
    private final RegisteredLearnersReportRequestProcessorService registeredLearnerReportRequestService;

    public Scheduler(CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService,
                     RegisteredLearnersReportRequestProcessorService registeredLearnerReportRequestService) {
        this.courseCompletionReportRequestProcessorService = courseCompletionReportRequestProcessorService;
        this.registeredLearnerReportRequestService = registeredLearnerReportRequestService;
    }

    @Scheduled(cron = "${report-export.courseCompletions.jobCron}")
    @SchedulerLock(name = "courseCompletionsReportJob", lockAtMostFor = "PT4H")
    public void generateReportsForCourseCompletionRequests() throws IOException {
        LockAssert.assertLocked();
        log.info("Starting job for course completion report requests");
        courseCompletionReportRequestProcessorService.processRequests();
        log.info("Finished job for course completion report requests");
    }

    @Scheduled(cron = "${report-export.registeredLearners.jobCron}")
    @SchedulerLock(name = "registeredLearnersReportJob", lockAtMostFor = "PT4H")
    public void generateReportsForRegisteredLearnerRequests() throws IOException {
        LockAssert.assertLocked();
        log.info("Starting job for course registered learner requests");
        registeredLearnerReportRequestService.processRequests();
        log.info("Finished job for course registered learner requests");
    }
}
