package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.learnerRecord.ILearnerRecordClient;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.report.CsvData;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.blob.DownloadableFile;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionCsvRowFactory;
import uk.gov.cshr.report.service.reportRequests.export.LearnerRecordCourseCompletionsCsvType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

@Service
@Slf4j
public class LearnerRecordService {

    private final ILearnerRecordClient learnerRecordClient;
    private final CourseCompletionEventRepository courseCompletionEventRepository;
    private final CourseCompletionCsvRowFactory courseCompletionCsvRowFactory;
    private final IUserAuthService userAuthService;
    private final Clock clock;
    private final CsvService csvService;
    private final ZipService zipService;

    public LearnerRecordService(ILearnerRecordClient learnerRecordClient,
                                CourseCompletionEventRepository courseCompletionEventRepository,
                                CourseCompletionCsvRowFactory courseCompletionCsvRowFactory,
                                IUserAuthService userAuthService,
                                Clock clock,
                                CsvService csvService,
                                ZipService zipService) {
        this.learnerRecordClient = learnerRecordClient;
        this.courseCompletionEventRepository = courseCompletionEventRepository;
        this.courseCompletionCsvRowFactory = courseCompletionCsvRowFactory;
        this.userAuthService = userAuthService;
        this.clock = clock;
        this.csvService = csvService;
        this.zipService = zipService;
    }

    public DownloadableFile exportLearnerRecord() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        return exportLearnerRecord(userAuthService.getUsername());
    }

    public DownloadableFile exportLearnerRecord(String userId) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.info("Exporting learner record for user {}", userId);
        return createLearnerRecordZipFile(courseCompletionEventRepository.findByUserId(userId));
    }

    private DownloadableFile createLearnerRecordZipFile(List<CourseCompletionEvent> courseCompletionEvents) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        CsvData<CourseCompletionEvent> csvData = courseCompletionCsvRowFactory.getCsvData(
                courseCompletionEvents, new LearnerRecordCourseCompletionsCsvType()
        );

        String baseFileName = String.format("learner_record_%s", now(clock).format(ofPattern("yyyy-MM-dd_HH-mm-ss")));
        String csvFileName = csvService.createCsvFile(csvData, baseFileName);
        String zipFileName = zipService.createZipFile(baseFileName, csvFileName);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Path zipFilePath = Path.of(zipFileName);
        Files.copy(zipFilePath, byteArrayOutputStream);

        log.info("Deleting csv file {}", csvFileName);
        Files.delete(Path.of(csvFileName));
        log.info("Deleting zip file {}", zipFileName);
        Files.delete(zipFilePath);

        return new DownloadableFile(zipFileName, byteArrayOutputStream);
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public List<LearnerRecordEvent> listEvents() {
        return learnerRecordClient.getLearnerRecordEvents();
    }

    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        return learnerRecordClient.getBookings(from, to);
    }

    public List<ModuleRecord> getModuleRecordsForLearners(LocalDate from, LocalDate to, List<String> learnerIds) {
        return learnerRecordClient.getModuleRecordsForDateRangeAndLearnerIds(from, to, learnerIds);
    }

    public List<ModuleRecord> getModulesRecordsForCourseIds(LocalDate from, LocalDate to, List<String> courseIds) {
        return learnerRecordClient.getModuleRecordsForCourseIds(from, to, courseIds);
    }
}
