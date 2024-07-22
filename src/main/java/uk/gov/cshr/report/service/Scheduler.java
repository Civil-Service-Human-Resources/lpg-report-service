package uk.gov.cshr.report.service;

import com.azure.storage.blob.BlobClient;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.*;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Scheduler {
    @Autowired
    private CourseCompletionReportRequestService courseCompletionReportRequestService;

    @Autowired
    private CourseCompletionService courseCompletionService;

    @Autowired
    private BlobStorageService blobStorageService;

    @Autowired
    private CourseCompletionsCsvService courseCompletionsCsvService;

    @Autowired
    private ZipService zipService;

    String directory = "temp-courseCompletionsJob";

    private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

    @Scheduled(cron = "${courseCompletions.reports.jobCron}")
    @SchedulerLock(name = "reportRequestsJob", lockAtMostFor = "PT4H")
    public void generateReportsForCourseCompletionRequests() {
        LockAssert.assertLocked();
        LOG.debug("Starting job for course completion report requests");

        List<CourseCompletionReportRequest> requests = courseCompletionReportRequestService.findAllRequestsByStatus(CourseCompletionReportRequestStatus.REQUESTED);
        LOG.debug(String.format("Found %d requests", requests.size()));

        for(CourseCompletionReportRequest request : requests) {
            try {
                processRequest(request);
            }
            catch (Exception e){
                processFailure(e, request.getReportRequestId());
            }
        }
    }

    public void processRequest(CourseCompletionReportRequest request) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        courseCompletionReportRequestService.setStatusForReportRequest(request.getReportRequestId(), CourseCompletionReportRequestStatus.PROCESSING);

        File tempDirectory = new File(directory);

        if(!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }

        String fileName = getFileName(request.getReportRequestId(), request.getFromDate(), request.getToDate());
        String csvFileName = String.format("%s/%s.csv", directory, fileName);
        String zipFileName = String.format("%s/%s.zip", directory, fileName);

        LOG.debug(String.format("Processing request: %s", request));

        List<CourseCompletionEvent> courseCompletions = getCourseCompletionsForRequest(request);

        createCsvFileFromCompletions(courseCompletions, csvFileName);

        zipService.createZipFile(zipFileName, csvFileName);

        blobStorageService.uploadFile(zipFileName);

        LOG.debug(String.format("Processing of request with ID %s has succeeded", request.getReportRequestId()));
        courseCompletionReportRequestService.setStatusForReportRequest(request.getReportRequestId(), CourseCompletionReportRequestStatus.SUCCESS);

        // TODO: Send success email
    }

    public void processFailure(Exception e, Long requestId){
        LOG.debug(String.format("Processing request %s has failed", requestId));
        LOG.debug(e.getMessage());
        e.printStackTrace();

        courseCompletionReportRequestService.setStatusForReportRequest(requestId, CourseCompletionReportRequestStatus.FAILED);

        // TODO: Send failure email
    }

    private List<CourseCompletionEvent> getCourseCompletionsForRequest(CourseCompletionReportRequest request){
        GetCourseCompletionsParams params = new GetCourseCompletionsParams();
        params.setCourseIds(request.getCourseIds());
        params.setOrganisationIds(request.getOrganisationIds());
        params.setStartDate(request.getFromDate().toLocalDateTime());
        params.setEndDate(request.getToDate().toLocalDateTime());

        return courseCompletionService.getCourseCompletionEvents(params);
    }

    private void createCsvFileFromCompletions(List<CourseCompletionEvent> courseCompletions, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<CourseCompletionCsv> csvRows = getCsvRowsFromCourseCompletionAggregations(courseCompletions);
        courseCompletionsCsvService.createCsvFile(csvRows, fileName);
    }

    private List<CourseCompletionCsv> getCsvRowsFromCourseCompletionAggregations(List<CourseCompletionEvent> courseCompletions){
        List<CourseCompletionCsv> eventCsvRows = new ArrayList<>();

        for (CourseCompletionEvent event: courseCompletions){
            eventCsvRows.add(new CourseCompletionCsv(
                    event.getEventId(),
                    event.getExternalId(),
                    event.getUserId(),
                    event.getUserEmail(),
                    event.getCourseId(),
                    event.getCourseTitle(),
                    event.getEventTimestamp(),
                    event.getOrganisationId(),
                    event.getOrganisationAbbreviation(),
                    event.getProfessionId(),
                    event.getProfessionName(),
                    event.getGradeId(),
                    event.getGradeCode()
                    ));
        }

        return eventCsvRows;
    }

    private String getFileName(Long requestId, ZonedDateTime fromDate, ZonedDateTime toDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("course_completions_%s_from_%s_to_%s", requestId, fromDate.format(formatter), toDate.format(formatter));
    }
}
