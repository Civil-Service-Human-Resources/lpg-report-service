package uk.gov.cshr.report.integration;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobContainerProperties;
import com.azure.storage.blob.models.BlobItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import uk.gov.cshr.report.configuration.TestConfig;
import uk.gov.cshr.report.domain.CourseCompletionReportRequest;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;
import uk.gov.cshr.report.service.Scheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(TestConfig.class)
public class CourseCompletionsReportRequestIntegrationTest extends IntegrationTestBase{
    private static String directory = "temp-courseCompletionsJob";
    private String zipFileName = "course_completions_1_from_01_01_2024_to_01_02_2024.zip";
    private String csvFileName = "course_completions_1_from_01_01_2024_to_01_02_2024.csv";
    private String blobStorageContainerName = "coursecompletionrequests";

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(new PGSimpleDataSource());

    @Autowired
    Scheduler scheduler;

    @Autowired
    CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;

    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String azureBlobStorageConnectionString;

    private static GenericContainer<?> azuriteContainer;

    @BeforeAll
    public static void beforeAll() {
        File dir = new File(directory);
        dir.delete();
        dir.mkdir();

        azuriteContainer = CustomAzuriteContainer.getInstance();
        azuriteContainer.start();
    }


    @BeforeEach
    public void setUp(){
        courseCompletionReportRequestRepository.deleteAll();
    }

    @AfterAll
    public static void afterAll(){
        if(azuriteContainer != null){
            azuriteContainer.stop();
        }
    }

    @Test
    public void testReportRequestsSchedulerProcessesJobCorrectlyWhenNoExceptionIsThrown() throws IOException {

        jdbcTemplate.execute("""
        INSERT INTO course_completion_report_requests
            (report_request_id, requester_id, requester_email, requested_timestamp, completed_timestamp, status, from_date, to_date, course_ids, organisation_ids)
            VALUES(1, 'RequesterA', 'RequesterA@domain.com', '2024-07-08 09:15:27.352', NULL, 'REQUESTED', '2024-01-01 00:00:00.000', '2024-02-01 00:00:00.000', '{c1,c2}', '{1}');
        """);

        scheduler.generateReportsForCourseCompletionRequests();

        // Tests:
        testSchedulerCreatesAzureBlogStorageContainerForCourseCompletionReportRequests();
        testSchedulerStoresZipFileInAzureBlogStorageWithCorrectName();
        testSchedulerSetsProcessedCourseCompletionReportRequestStatusToSuccess();
        testZipFileContainsCsvFile();
    }

    private void testSchedulerCreatesAzureBlogStorageContainerForCourseCompletionReportRequests(){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .buildClient();

        BlobContainerProperties properties = blobServiceClient.getBlobContainerClient(blobStorageContainerName).getProperties();
        assertNotNull(properties);
    }

    private void testSchedulerStoresZipFileInAzureBlogStorageWithCorrectName(){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .buildClient();

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(blobStorageContainerName);
        PagedIterable<BlobItem> blobs = blobContainerClient.listBlobs();

        int matches = 0;
        for(BlobItem blobItem : blobs) {
            if(blobItem.getName().equals(zipFileName)){
                matches++;
            }
        }
        assertEquals(1, matches);
    }

    private void testSchedulerSetsProcessedCourseCompletionReportRequestStatusToSuccess(){
        List<CourseCompletionReportRequest> successfulRequests = courseCompletionReportRequestRepository.findByStatus("SUCCESS");
        assertEquals(1, successfulRequests.size());
    }

    private void testZipFileContainsCsvFile() throws IOException {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .containerName(blobStorageContainerName)
                .blobName(zipFileName)
                .buildClient();

        String downloadZipFile = directory + "/downloadedContent.zip";
        new File(downloadZipFile).delete();
        blobClient.downloadToFile(downloadZipFile);

        ZipFile zipFile = new ZipFile(new File(downloadZipFile));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        List<String> fileNames = new ArrayList<>();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            fileNames.add(entry.getName());
        }

        assertEquals(1, fileNames.size());
        assertEquals(csvFileName, fileNames.get(0));
    }

    @Test
    public void testSchedulerSetsStatusOfReportRequestToFailedWhenJobFailsToComplete(){
        jdbcTemplate.execute("""
        INSERT INTO course_completion_report_requests
            (report_request_id, requester_id, requester_email, requested_timestamp, completed_timestamp, status, from_date, to_date, course_ids, organisation_ids)
            VALUES(1, 'RequesterA', 'RequesterA@domain.com', '2024-07-08 09:15:27.352', NULL, 'REQUESTED', '2024-01-01 00:00:00.000', '2024-02-01 00:00:00.000', '{c1,c2}', '{1}');
        """);

        scheduler.processFailure(new Exception(), 1L);

        List<CourseCompletionReportRequest> failedRequests = courseCompletionReportRequestRepository.findByStatus("FAILED");
        assertEquals(1, failedRequests.size());
    }
}
