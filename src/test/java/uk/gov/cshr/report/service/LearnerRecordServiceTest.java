package uk.gov.cshr.report.service;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cshr.report.client.learnerRecord.ILearnerRecordClient;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;
import uk.gov.cshr.report.repository.CourseCompletionEventRepository;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.blob.DownloadableFile;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionCsvRowFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LearnerRecordServiceTest {
    private LearnerRecordService learnerRecordService;

    private ILearnerRecordClient learnerRecordClient;
    private CourseCompletionEventRepository courseCompletionEventRepository;
    private IUserAuthService userAuthService;

    @BeforeEach
    public void setUp() {
        learnerRecordClient = mock(ILearnerRecordClient.class);
        courseCompletionEventRepository = mock(CourseCompletionEventRepository.class);
        userAuthService = mock(IUserAuthService.class);
        CourseCompletionCsvRowFactory courseCompletionCsvRowFactory = new CourseCompletionCsvRowFactory();
        Clock clock = Clock.fixed(Instant.parse("2024-05-10T14:30:00.000Z"), ZoneId.of("UTC"));
        CsvService csvService = new CsvService();
        ZipService zipService = new ZipService();

        learnerRecordService = new LearnerRecordService(learnerRecordClient, courseCompletionEventRepository,
                courseCompletionCsvRowFactory, userAuthService, clock, csvService, zipService);
    }

    @Test
    public void testLearnerRecordServiceGetBookingReturnsListOfBookingsReturnedFromClient() {
        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        Booking booking1 = new Booking();
        booking1.setLearner("Learner Name");
        booking1.setEventUid("Example event");
        booking1.setStatus(BookingStatus.CONFIRMED);

        Booking booking2 = new Booking();
        booking2.setLearner("Another Learner");
        booking2.setEventUid("Another event");
        booking2.setStatus(BookingStatus.CANCELLED);
        List<Booking> fakeBookingList = new ArrayList<>();
        fakeBookingList.add(booking1);
        fakeBookingList.add(booking2);

        when(learnerRecordClient.getBookings(from,to)).thenReturn(fakeBookingList);

        List<Booking> bookingListFromLearnerRecordService = learnerRecordService.getBookings(from, to);

        assertEquals(2, bookingListFromLearnerRecordService.size());
        assertEquals("Learner Name", bookingListFromLearnerRecordService.get(0).getLearner());
        assertEquals("Example event", bookingListFromLearnerRecordService.get(0).getEventUid());
        assertEquals(BookingStatus.CONFIRMED, bookingListFromLearnerRecordService.get(0).getStatus());
        assertEquals("Another Learner", bookingListFromLearnerRecordService.get(1).getLearner());
        assertEquals("Another event", bookingListFromLearnerRecordService.get(1).getEventUid());
        assertEquals(BookingStatus.CANCELLED, bookingListFromLearnerRecordService.get(1).getStatus());
    }

    @Test
    public void testExportLearnerRecordReturnsZipWithCsvData() throws Exception {
        String userId = "testUserId123";
        when(userAuthService.getUsername()).thenReturn(userId);

        CourseCompletionEvent event = new CourseCompletionEvent(
                "1",
                userId,
                "learner@example.com",
                "course123",
                "Sample Course",
                LocalDateTime.of(2024, 5, 10, 14, 30, 0),
                1,
                "Cabinet Office",
                2,
                "Digital",
                3,
                "Grade 7"
        );

        when(courseCompletionEventRepository.findByUserId(userId)).thenReturn(List.of(event));

        DownloadableFile downloadableFile = learnerRecordService.exportLearnerRecord();

        assertNotNull(downloadableFile);
        assertEquals("learner_record_2024-05-10_14-30-00.zip", downloadableFile.getFileName());

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(downloadableFile.getBytes().toByteArray()))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals("learner_record_2024-05-10_14-30-00.csv", entry.getName());

            CSVReader reader = new CSVReader(new InputStreamReader(zis));
            List<String[]> rows = reader.readAll();

            assertEquals(2, rows.size());

            String[] header = rows.get(0);
            assertEquals("userId", header[0]);
            assertEquals("userEmail", header[1]);
            assertEquals("courseId", header[2]);
            assertEquals("courseTitle", header[3]);
            assertEquals("eventTimestamp", header[4]);
            assertEquals("organisationId", header[5]);
            assertEquals("organisationName", header[6]);
            assertEquals("professionId", header[7]);
            assertEquals("professionName", header[8]);
            assertEquals("gradeId", header[9]);
            assertEquals("gradeName", header[10]);

            String[] dataRow = rows.get(1);
            assertEquals("testUserId123", dataRow[0]);
            assertEquals("learner@example.com", dataRow[1]);
            assertEquals("course123", dataRow[2]);
            assertEquals("Sample Course", dataRow[3]);
            assertEquals("2024-05-10T14:30", dataRow[4]);
            assertEquals("1", dataRow[5]);
            assertEquals("Cabinet Office", dataRow[6]);
            assertEquals("2", dataRow[7]);
            assertEquals("Digital", dataRow[8]);
            assertEquals("3", dataRow[9]);
            assertEquals("Grade 7", dataRow[10]);
        }

        assertFalse(Files.exists(Path.of("learner_record_2024-05-10_14-30-00.csv")));
        assertFalse(Files.exists(Path.of("learner_record_2024-05-10_14-30-00.zip")));
    }

    @Test
    public void testExportLearnerRecordWithUserIdReturnsZipWithCsvData() throws Exception {
        String userId = "testUserId456";

        CourseCompletionEvent event = new CourseCompletionEvent(
                "2",
                userId,
                "learner2@example.com",
                "course456",
                "Sample Course 2",
                LocalDateTime.of(2024, 5, 10, 14, 30, 0),
                1,
                "Cabinet Office",
                2,
                "Digital",
                3,
                "Grade 7"
        );

        when(courseCompletionEventRepository.findByUserId(userId)).thenReturn(List.of(event));

        DownloadableFile downloadableFile = learnerRecordService.exportLearnerRecord(userId);

        assertNotNull(downloadableFile);
        assertEquals("learner_record_2024-05-10_14-30-00.zip", downloadableFile.getFileName());

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(downloadableFile.getBytes().toByteArray()))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals("learner_record_2024-05-10_14-30-00.csv", entry.getName());

            CSVReader reader = new CSVReader(new InputStreamReader(zis));
            List<String[]> rows = reader.readAll();

            assertEquals(2, rows.size());

            String[] header = rows.get(0);
            assertEquals("userId", header[0]);
            assertEquals("userEmail", header[1]);
            assertEquals("courseId", header[2]);
            assertEquals("courseTitle", header[3]);

            String[] dataRow = rows.get(1);
            assertEquals("testUserId456", dataRow[0]);
            assertEquals("learner2@example.com", dataRow[1]);
            assertEquals("course456", dataRow[2]);
            assertEquals("Sample Course 2", dataRow[3]);
        }

        assertFalse(Files.exists(Path.of("learner_record_2024-05-10_14-30-00.csv")));
        assertFalse(Files.exists(Path.of("learner_record_2024-05-10_14-30-00.zip")));
    }
}
