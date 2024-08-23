package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.CourseCompletionCsv;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.CourseCompletionReportRequestStatus;
import uk.gov.cshr.report.dto.MessageDto;
import uk.gov.cshr.report.service.blob.BlobStorageService;
import uk.gov.cshr.report.service.blob.UploadResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Setter
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

    @Autowired
    private NotificationService notificationService;

    @Value("${courseCompletions.reports.defaultTimezone}")
    private String defaultTimezone;

    @Value("${courseCompletions.reports.daysToKeepReportLinkActive}")
    private Integer daysToKeepReportLinkActive;

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
            LOG.debug("Processing request {}", request.getRequesterId());
            try {
                processRequest(request);
            }
            catch (Exception e){
                processFailure(e, request.getReportRequestId(), request.getRequesterEmail());
            }
            finally {
                courseCompletionReportRequestService.setCompletedDateForReportRequest(request.getReportRequestId(), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
                cleanUp();
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

        String timezone = request.getRequesterTimezone() == null ? defaultTimezone : request.getRequesterTimezone();
        createCsvFileFromCompletions(courseCompletions, csvFileName, timezone);

        zipService.createZipFile(zipFileName, csvFileName);

        UploadResult uploadResult = blobStorageService.uploadFileAndGenerateDownloadLink(zipFileName, daysToKeepReportLinkActive);

        LOG.debug(String.format("Processing of request with ID %s has succeeded", request.getReportRequestId()));
        courseCompletionReportRequestService.setStatusForReportRequest(request.getReportRequestId(), CourseCompletionReportRequestStatus.SUCCESS);

        LOG.debug(String.format("Sending success email to %s", request.getRequesterEmail()));
        sendSuccessEmail(request.getRequesterEmail(), uploadResult.getDownloadUrl());
        LOG.debug(String.format("Success email sent to %s", request.getRequesterEmail()));
    }

    public void processFailure(Exception e, Long requestId, String requesterEmail){
        LOG.debug(String.format("Processing request %s has failed", requestId), e);

        courseCompletionReportRequestService.setStatusForReportRequest(requestId, CourseCompletionReportRequestStatus.FAILED);

        LOG.debug(String.format("Sending failure email to %s", requesterEmail));
        sendFailureEmail(requesterEmail);
        LOG.debug(String.format("Failure email sent to %s", requesterEmail));
    }

    private List<CourseCompletionEvent> getCourseCompletionsForRequest(CourseCompletionReportRequest request){
        GetCourseCompletionsParams params = new GetCourseCompletionsParams();
        params.setCourseIds(request.getCourseIds());
        params.setOrganisationIds(request.getOrganisationIds());
        params.setStartDate(request.getFromDate().toLocalDateTime());
        params.setEndDate(request.getToDate().toLocalDateTime());

        return courseCompletionService.getCourseCompletionEvents(params);
    }

    private void createCsvFileFromCompletions(List<CourseCompletionEvent> courseCompletions, String fileName, String timezone) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<CourseCompletionCsv> csvRows = getCsvRowsFromCourseCompletions(courseCompletions, timezone);
        courseCompletionsCsvService.createCsvFile(csvRows, fileName);
    }

    private List<CourseCompletionCsv> getCsvRowsFromCourseCompletions(List<CourseCompletionEvent> courseCompletions, String timezone){
        List<CourseCompletionCsv> eventCsvRows = new ArrayList<>();

        for (CourseCompletionEvent event: courseCompletions){
            eventCsvRows.add(new CourseCompletionCsv(
                    event.getEventId(),
                    event.getExternalId(),
                    event.getUserId(),
                    event.getUserEmail(),
                    event.getCourseId(),
                    event.getCourseTitle(),
                    convertZonedDateTimeToTimezone(event.getEventTimestamp(), timezone),
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

    private LocalDateTime convertZonedDateTimeToTimezone(LocalDateTime dateTime, String timezone){
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("UTC"));

        ZoneId targetZoneId = ZoneId.of(timezone);
        ZonedDateTime targetZonedDateTime = zonedDateTime.withZoneSameInstant(targetZoneId);
        return targetZonedDateTime.toLocalDateTime();
    }

    private String getFileName(Long requestId, ZonedDateTime fromDate, ZonedDateTime toDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return String.format("course_completions_%s_from_%s_to_%s", requestId, fromDate.format(formatter), toDate.format(formatter));
    }

    private void sendSuccessEmail(String email, String blobUrl){
        MessageDto messageDto = new MessageDto();
        messageDto.setRecipient(email);
        Map<String, String> personalisation = new HashMap<>();
        personalisation.put("reportUrl", blobUrl);
        messageDto.setPersonalisation(personalisation);
        notificationService.sendSuccessEmail(messageDto);
    }

    private void sendFailureEmail(String email){
        MessageDto messageDto = new MessageDto();
        messageDto.setRecipient(email);
        notificationService.sendFailureEmail(messageDto);
    }

    private void cleanUp(){
        File tempDirectory = new File(directory);

        if(!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }
    }
}
