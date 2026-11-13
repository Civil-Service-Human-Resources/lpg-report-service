package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LearnerRecordService {

    private final ILearnerRecordClient learnerRecordClient;
    private final CourseCompletionEventRepository courseCompletionEventRepository;
    private final CourseCompletionCsvRowFactory courseCompletionCsvRowFactory;
    private final IUserAuthService userAuthService;
    private final Clock clock;

    public LearnerRecordService(ILearnerRecordClient learnerRecordClient,
                                CourseCompletionEventRepository courseCompletionEventRepository,
                                CourseCompletionCsvRowFactory courseCompletionCsvRowFactory,
                                IUserAuthService userAuthService,
                                Clock clock) {
        this.learnerRecordClient = learnerRecordClient;
        this.courseCompletionEventRepository = courseCompletionEventRepository;
        this.courseCompletionCsvRowFactory = courseCompletionCsvRowFactory;
        this.userAuthService = userAuthService;
        this.clock = clock;
    }

    public DownloadableFile exportLearnerRecord() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String userId = userAuthService.getUsername();
        return exportLearnerRecord(userId);
    }

    public DownloadableFile exportLearnerRecord(String userId) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<CourseCompletionEvent> events = courseCompletionEventRepository.findByUserId(userId);
        return createLearnerRecordZipFile(events);
    }

    private DownloadableFile createLearnerRecordZipFile(List<CourseCompletionEvent> events) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String formattedDateTime = LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String baseFileName = String.format("learner_record_%s", formattedDateTime);
        String csvFileName = String.format("%s.csv", baseFileName);
        String zipFileName = String.format("%s.zip", baseFileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry zipEntry = new ZipEntry(csvFileName);
            zos.putNextEntry(zipEntry);

            CsvData<CourseCompletionEvent> csvData = courseCompletionCsvRowFactory.getCsvData(
                    events, new LearnerRecordCourseCompletionsCsvType()
            );

            Writer writer = new OutputStreamWriter(zos, StandardCharsets.UTF_8);
            csvData.write(writer);
            writer.flush();
            zos.closeEntry();
        }

        return new DownloadableFile(zipFileName, baos);
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
