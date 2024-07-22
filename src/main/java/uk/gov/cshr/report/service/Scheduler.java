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
import uk.gov.cshr.report.domain.CourseCompletionAggregationCsv;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.CourseCompletionReportRequestStatus;
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

        List<CourseCompletionAggregation> courseCompletions = getCourseCompletionsForRequest(request);

        createCsvFileFromCompletions(courseCompletions, csvFileName);

        zipService.createZipFile(zipFileName, csvFileName);

        BlobClient blobClient = blobStorageService.uploadFile(zipFileName);
        System.out.println("::: Blob URL: " + blobClient.getBlobUrl());

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

    private List<CourseCompletionAggregation> getCourseCompletionsForRequest(CourseCompletionReportRequest request){
        GetCourseCompletionsParams params = new GetCourseCompletionsParams();
        params.setCourseIds(request.getCourseIds());
        params.setOrganisationIds(request.getOrganisationIds());
        params.setStartDate(request.getFromDate().toLocalDate());
        params.setEndDate(request.getToDate().toLocalDate());

        return courseCompletionService.getCourseCompletions(params);
    }

    private void createCsvFileFromCompletions(List<CourseCompletionAggregation> courseCompletions, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<CourseCompletionAggregationCsv> csvRows = getCsvRowsFromCourseCompletionAggregations(courseCompletions);
        courseCompletionsCsvService.createCsvFile(csvRows, fileName);
    }

    private List<CourseCompletionAggregationCsv> getCsvRowsFromCourseCompletionAggregations(List<CourseCompletionAggregation> courseCompletions){
        List<CourseCompletionAggregationCsv> aggregationCsvRows = new ArrayList<>();

        for (CourseCompletionAggregation a: courseCompletions){
            aggregationCsvRows.add(new CourseCompletionAggregationCsv(a.getCourseId(), a.getdateBin(), a.getTotal()));
        }

        return aggregationCsvRows;
    }

    private String getFileName(Long requestId, ZonedDateTime fromDate, ZonedDateTime toDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("course_completions_%s_from_%s_to_%s", requestId, fromDate.format(formatter), toDate.format(formatter));
    }
}
