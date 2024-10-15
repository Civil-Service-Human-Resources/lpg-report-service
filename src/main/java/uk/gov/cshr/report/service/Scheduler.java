package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.controller.model.GetCourseCompletionsParams;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionCsv;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequestStatus;
import uk.gov.cshr.report.dto.MessageDto;
import uk.gov.cshr.report.service.blob.BlobStorageService;
import uk.gov.cshr.report.service.blob.UploadResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    @Scheduled(cron = "${courseCompletions.reports.jobCron}")
    @SchedulerLock(name = "reportRequestsJob", lockAtMostFor = "PT4H")
    public void generateReportsForCourseCompletionRequests() {
        LockAssert.assertLocked();
        log.info("Starting job for course completion report requests");

        List<CourseCompletionReportRequest> requests = courseCompletionReportRequestService.findAllRequestsByStatus(CourseCompletionReportRequestStatus.REQUESTED);
        log.info(String.format("Found %d requests", requests.size()));

        for(CourseCompletionReportRequest request : requests) {
            log.info("Processing request {}", request.getRequesterId());
            try {
                processRequest(request);
            }
            catch (Exception e){
                processFailure(e, request);
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

        log.debug(String.format("Processing request: %s", request));

        List<CourseCompletionEvent> courseCompletions = getCourseCompletionsForRequest(request);

        String timezone = request.getRequesterTimezone() == null ? defaultTimezone : request.getRequesterTimezone();
        createCsvFileFromCompletions(courseCompletions, csvFileName, timezone);

        zipService.createZipFile(zipFileName, csvFileName);

        UploadResult uploadResult = blobStorageService.uploadFileAndGenerateDownloadLink(zipFileName, daysToKeepReportLinkActive);

        log.info(String.format("Processing of request with ID %s has succeeded", request.getReportRequestId()));
        courseCompletionReportRequestService.setStatusForReportRequest(request.getReportRequestId(), CourseCompletionReportRequestStatus.SUCCESS);

        log.info(String.format("Sending success email to %s", request.getRequesterEmail()));
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZoneOffset targetZoneOffset = ZoneOffset.of(request.getRequesterTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");
        String formattedDateTime = zonedDateTime.withZoneSameInstant(targetZoneOffset).format(formatter);
        sendSuccessEmail(request.getRequesterEmail(), request.getFullName(), uploadResult.getDownloadUrl(), formattedDateTime);
        log.info(String.format("Success email sent to %s", request.getRequesterEmail()));
    }

    public void processFailure(Exception e, CourseCompletionReportRequest request){
        log.info(String.format("Processing request %s has failed", request.getReportRequestId()), e);

        courseCompletionReportRequestService.setStatusForReportRequest(request.getReportRequestId(), CourseCompletionReportRequestStatus.FAILED);

        log.debug(String.format("Sending failure email to %s", request.getRequesterEmail()));
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZoneOffset targetZoneOffset = ZoneOffset.of(request.getRequesterTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");
        String formattedDateTime = zonedDateTime.withZoneSameInstant(targetZoneOffset).format(formatter);
        sendFailureEmail(request.getRequesterEmail(), request.getFullName(), formattedDateTime);
        log.debug(String.format("Failure email sent to %s", request.getRequesterEmail()));
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
                    event.getUserEmail(),
                    event.getCourseId(),
                    event.getCourseTitle(),
                    convertZonedDateTimeToTimezone(event.getEventTimestamp(), timezone),
                    event.getOrganisationId(),
                    event.getOrganisationName(),
                    event.getProfessionId(),
                    event.getProfessionName(),
                    event.getGradeId(),
                    event.getGradeName()
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

    private void sendSuccessEmail(String email, String requesterFullName, String blobUrl, String time){
        MessageDto messageDto = new MessageDto();
        messageDto.setRecipient(email);

        Map<String, String> personalisation = new HashMap<>();
        personalisation.put("userName", requesterFullName);
        personalisation.put("exportDate", time);
        personalisation.put("reportUrl", blobUrl);
        personalisation.put("reportType", "Course completions");

        messageDto.setPersonalisation(personalisation);
        notificationService.sendSuccessEmail(messageDto);
    }

    private void sendFailureEmail(String email, String requesterFullName, String time){
        MessageDto messageDto = new MessageDto();
        messageDto.setRecipient(email);

        Map<String, String> personalisation = new HashMap<>();
        personalisation.put("userName", requesterFullName);
        personalisation.put("exportDate", time);
        personalisation.put("reportType", "Course completions");

        messageDto.setPersonalisation(personalisation);
        notificationService.sendFailureEmail(messageDto);
    }

    private void cleanUp(){
        File tempDirectory = new File(directory);

        if(!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }
    }
}
