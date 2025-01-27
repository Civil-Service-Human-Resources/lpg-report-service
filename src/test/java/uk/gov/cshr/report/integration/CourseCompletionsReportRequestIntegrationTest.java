package uk.gov.cshr.report.integration;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerProperties;
import com.azure.storage.blob.models.BlobItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import uk.gov.cshr.report.configuration.TestConfig;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequestStatus;
import uk.gov.cshr.report.repository.CourseCompletionReportRequestRepository;
import uk.gov.cshr.report.service.CourseCompletionReportRequestProcessorService;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.auth.UserAuthService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(TestConfig.class)
public class CourseCompletionsReportRequestIntegrationTest extends IntegrationTestBase {
    private String zipFileName = "course_completions_1_from_01_01_2024_to_01_02_2024.zip";
    private String csvFileName = "course_completions_1_from_01_01_2024_to_01_02_2024.csv";

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(new PGSimpleDataSource());

    @Autowired
    private CourseCompletionReportRequestRepository courseCompletionReportRequestRepository;

    @Autowired
    private CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;

    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String azureBlobStorageConnectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String blobStorageContainerName;

    @TempDir
    private Path tempDirectory;

    private static GenericContainer<?> azuriteContainer;

    @MockBean
    IUserAuthService userAuthService;

    @BeforeAll
    public static void beforeAll() {
        azuriteContainer = CustomAzuriteContainer.getInstance();
        azuriteContainer.start();
    }

    @BeforeEach
    public void setUp(){
        BlobServiceClient blobServiceClient = getBlobServiceClient();
        blobServiceClient.deleteBlobContainerIfExists(blobStorageContainerName);
        courseCompletionReportRequestRepository.deleteAll();
    }

    @AfterAll
    public static void afterAll(){
        if(azuriteContainer != null){
            azuriteContainer.stop();
        }
    }

    private BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .buildClient();
    }

    private void insertCourseCompletionRequest() {
        jdbcTemplate.execute("""
        INSERT INTO course_completion_report_requests
            (report_request_id, requester_id, requester_email, requested_timestamp, completed_timestamp, status, from_date, to_date, course_ids, organisation_ids, requester_timezone, requester_full_name, url_slug, download_base_url, detailed_export)
            VALUES(1, 'userId', 'RequesterA@domain.com', '2024-07-08 09:15:27.352', NULL, 'REQUESTED', '2024-01-01 00:00:00.000', '2024-02-01 00:00:00.000', '{c1,c2}', '{1}', '+01:00', 'Requester A', 'slug', 'http://base.com', FALSE);
        """);
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsReturnsAddedSuccessfullyTrueResponse() throws Exception {
        List<String> fakeAuthorities = new ArrayList<>();
        fakeAuthorities.add("AUTHORITY1");
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", fakeAuthorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "value1");
        Jwt jwt = new Jwt("tokenValue", null, null, headers, claims);
        when(userAuthService.getBearerTokenFromUserAuth()).thenReturn(jwt);
        String postReportRequestsEndpoint = "/course-completions/report-requests";

        String requestBody = """
                {
                    "userId": "testUser01",
                    "userEmail": "user01@domain.com",
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-01T00:00:00",
                    "courseIds": ["course1", "course2"],
                    "organisationIds": [1,2,3,4],
                    "professionIds": [5,6,7,8],
                    "gradeIds": [4,3,2],
                    "timezone": "+1",
                    "downloadBaseUrl": "https://base.com"
                }""";

        String expectedResponseKey = "addedSuccessfully";
        Boolean expectedResponseValue = true;

        mockMvc.perform(post(postReportRequestsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath(String.format("$.%s", expectedResponseKey)).value(expectedResponseValue));
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsReturnsReturnsAddedSuccessfullyFalseResponseWithDetailsIfMaxLimitReachedForUser() throws Exception {
        List<String> fakeAuthorities = new ArrayList<>();
        fakeAuthorities.add("AUTHORITY1");
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", fakeAuthorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "value1");
        Jwt jwt = new Jwt("tokenValue", null, null, headers, claims);
        when(userAuthService.getBearerTokenFromUserAuth()).thenReturn(jwt);

        String postReportRequestsEndpoint = "/course-completions/report-requests";

        String requestBody = """
                {
                    "userId": "testUser02",
                    "userEmail": "user02@domain.com",
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-01T00:00:00",
                    "courseIds": ["course1", "course2"],
                    "organisationIds": [1,2,3,4],
                    "professionIds": [5,6,7,8],
                    "gradeIds": [4,3,2],
                    "timezone": "+1",
                    "downloadBaseUrl": "https://base.com"
                }""";

        String expectedAddedSuccessfullyResponseKey = "addedSuccessfully";
        Boolean expectedAddedSuccessfullyResponseValue = false;
        String expectedDetailsResponseKey = "details";
        String expectedDetailsResponseValue = "User has reached the maximum allowed report requests";

        mockMvc.perform(post(postReportRequestsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(post(postReportRequestsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath(String.format("$.%s", expectedAddedSuccessfullyResponseKey)).value(expectedAddedSuccessfullyResponseValue))
                .andExpect(jsonPath(String.format("$.%s", expectedDetailsResponseKey)).value(expectedDetailsResponseValue));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetReportRequestsReturnsCorrectListOfReportRequests() throws Exception {
        List<String> fakeAuthorities = new ArrayList<>();
        fakeAuthorities.add("AUTHORITY1");
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", fakeAuthorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "value1");
        Jwt jwt = new Jwt("tokenValue", null, null, headers, claims);
        when(userAuthService.getBearerTokenFromUserAuth()).thenReturn(jwt);

        String reportRequestsEndpoint = "/course-completions/report-requests";

        String postRequestBody = """
                {
                    "userId": "testUser03",
                    "userEmail": "user03@domain.com",
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-01T00:00:00",
                    "courseIds": ["course1", "course2"],
                    "organisationIds": [1,2,3,4],
                    "professionIds": [5,6,7,8],
                    "gradeIds": [4,3,2],
                    "timezone": "+1",
                    "downloadBaseUrl": "https://base.com"
                }""";

        mockMvc.perform(post(reportRequestsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postRequestBody))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(reportRequestsEndpoint)
                        .queryParam("userId", "testUser03")
                        .queryParam("status", "REQUESTED"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.requests[0].requesterId").value("testUser03"))
                .andExpect(jsonPath("$.requests[0].requesterEmail").value("user03@domain.com"))
                .andExpect(jsonPath("$.requests[0].status").value("REQUESTED"))
                .andExpect(jsonPath("$.requests[0].fromDate").value("2024-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.requests[0].toDate").value("2024-02-01T00:00:00Z"))
                .andExpect(jsonPath("$.requests[0].courseIds[0]").value("course1"))
                .andExpect(jsonPath("$.requests[0].courseIds[1]").value("course2"))
                .andExpect(jsonPath("$.requests[0].organisationIds[0]").value(1))
                .andExpect(jsonPath("$.requests[0].organisationIds[1]").value(2))
                .andExpect(jsonPath("$.requests[0].organisationIds[2]").value(3))
                .andExpect(jsonPath("$.requests[0].organisationIds[3]").value(4))
                .andExpect(jsonPath("$.requests[0].professionIds[0]").value(5))
                .andExpect(jsonPath("$.requests[0].professionIds[1]").value(6))
                .andExpect(jsonPath("$.requests[0].professionIds[2]").value(7))
                .andExpect(jsonPath("$.requests[0].professionIds[3]").value(8))
                .andExpect(jsonPath("$.requests[0].gradeIds[0]").value(4))
                .andExpect(jsonPath("$.requests[0].gradeIds[1]").value(3))
                .andExpect(jsonPath("$.requests[0].gradeIds[2]").value(2))
                .andExpect(jsonPath("$.requests[0].detailedExport").value(false));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetReportRequestsReturnsCorrectListOfReportRequestsWithDetailedExportSetToTrueForAppropriateRoles() throws Exception {
        List<String> fakeAuthorities = new ArrayList<>();
        fakeAuthorities.add("REPORT_EXPORT_DETAILED");
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", fakeAuthorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "value1");
        Jwt jwt = new Jwt("tokenValue", null, null, headers, claims);
        when(userAuthService.getBearerTokenFromUserAuth()).thenReturn(jwt);

        String reportRequestsEndpoint = "/course-completions/report-requests";

        String postRequestBody = """
                {
                    "userId": "testUser03",
                    "userEmail": "user03@domain.com",
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-01T00:00:00",
                    "courseIds": ["course1", "course2"],
                    "organisationIds": [1,2,3,4],
                    "professionIds": [5,6,7,8],
                    "gradeIds": [4,3,2],
                    "timezone": "+1",
                    "downloadBaseUrl": "https://base.com"
                }""";

        mockMvc.perform(post(reportRequestsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postRequestBody))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(reportRequestsEndpoint)
                        .queryParam("userId", "testUser03")
                        .queryParam("status", "REQUESTED"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.requests[0].requesterId").value("testUser03"))
                .andExpect(jsonPath("$.requests[0].requesterEmail").value("user03@domain.com"))
                .andExpect(jsonPath("$.requests[0].status").value("REQUESTED"))
                .andExpect(jsonPath("$.requests[0].fromDate").value("2024-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.requests[0].toDate").value("2024-02-01T00:00:00Z"))
                .andExpect(jsonPath("$.requests[0].courseIds[0]").value("course1"))
                .andExpect(jsonPath("$.requests[0].courseIds[1]").value("course2"))
                .andExpect(jsonPath("$.requests[0].organisationIds[0]").value(1))
                .andExpect(jsonPath("$.requests[0].organisationIds[1]").value(2))
                .andExpect(jsonPath("$.requests[0].organisationIds[2]").value(3))
                .andExpect(jsonPath("$.requests[0].organisationIds[3]").value(4))
                .andExpect(jsonPath("$.requests[0].professionIds[0]").value(5))
                .andExpect(jsonPath("$.requests[0].professionIds[1]").value(6))
                .andExpect(jsonPath("$.requests[0].professionIds[2]").value(7))
                .andExpect(jsonPath("$.requests[0].professionIds[3]").value(8))
                .andExpect(jsonPath("$.requests[0].gradeIds[0]").value(4))
                .andExpect(jsonPath("$.requests[0].gradeIds[1]").value(3))
                .andExpect(jsonPath("$.requests[0].gradeIds[2]").value(2))
                .andExpect(jsonPath("$.requests[0].detailedExport").value(true));
    }

    @Test
    public void testGetReportForValidUser() throws Exception {
        when(userAuthService.getUsername()).thenReturn("userId");

        insertCourseCompletionRequest();
        File testZipFile = new File(String.format("src/test/resources/report/%s", zipFileName));
        BlobContainerClient blobContainerClient = getBlobServiceClient().getBlobContainerClient(blobStorageContainerName);
        blobContainerClient.createIfNotExists();
        blobContainerClient.getBlobClient(zipFileName).uploadFromFile(testZipFile.getAbsolutePath());
        mockMvc
                .perform(MockMvcRequestBuilders.get("/course-completions/report-requests/downloads/slug"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", zipFileName)))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(Files.readAllBytes(testZipFile.toPath())));
        assertEquals(1, courseCompletionReportRequestRepository.findByUrlSlug("slug").get().getTimesDownloaded());
    }

    @Test
    public void testGetReportForInvalidUser() throws Exception {
        jdbcTemplate.execute("""
        INSERT INTO course_completion_report_requests
            (report_request_id, requester_id, requester_email, requested_timestamp, completed_timestamp, status, from_date, to_date, course_ids, organisation_ids, requester_timezone, requester_full_name, url_slug, download_base_url)
            VALUES(1, 'otherUser', 'RequesterA@domain.com', '2024-07-08 09:15:27.352', NULL, 'REQUESTED', '2024-01-01 00:00:00.000', '2024-02-01 00:00:00.000', '{c1,c2}', '{1}', '+01:00', 'Requester A', 'slug', 'http://base.com');
        """);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/course-completions/report-requests/downloads/slug"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("User is not authorised to download this report"));
    }

    @Test
    public void testGetReportForNotFoundReport() throws Exception {
        insertCourseCompletionRequest();
        mockMvc
                .perform(MockMvcRequestBuilders.get("/course-completions/report-requests/downloads/other-slug"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReportRequestsServiceProcessesJobCorrectlyWhenNoExceptionIsThrown() throws IOException {
        insertCourseCompletionRequest();
        stubService.stubSendEmail("COURSE_COMPLETIONS_REPORT_SUCCESS", """
                {
                    "recipient": "RequesterA@domain.com",
                    "personalisation": {
                        "userName": "Requester A",
                        "exportDate": "1 January 2024 11:00",
                        "reportUrl": "http://base.com/slug",
                        "reportType": "Course completions"
                    },
                    "reference": "test-uid"
                }
                """);
        courseCompletionReportRequestProcessorService.processRequests();

        // Tests:
        testSchedulerCreatesAzureBlobStorageContainerForCourseCompletionReportRequests();
        testSchedulerStoresZipFileInAzureBlobStorageWithCorrectName();
        testSchedulerSetsProcessedCourseCompletionReportRequestStatusToSuccess();
        testZipFileContainsCsvFile();
    }

    private void testSchedulerCreatesAzureBlobStorageContainerForCourseCompletionReportRequests(){
        BlobServiceClient blobServiceClient = getBlobServiceClient();

        BlobContainerProperties properties = blobServiceClient.getBlobContainerClient(blobStorageContainerName).getProperties();
        assertNotNull(properties);
    }

    private void testSchedulerStoresZipFileInAzureBlobStorageWithCorrectName(){
        BlobServiceClient blobServiceClient = getBlobServiceClient();

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
        List<CourseCompletionReportRequest> successfulRequests = courseCompletionReportRequestRepository.findByStatus(CourseCompletionReportRequestStatus.SUCCESS);
        assertEquals(1, successfulRequests.size());
    }

    private void testZipFileContainsCsvFile() throws IOException {
        BlobClient blobClient = getBlobServiceClient()
                .getBlobContainerClient(blobStorageContainerName)
                .getBlobClient(zipFileName);

        String downloadZipFile = String.format("%s/temp-downloadedContent.zip", tempDirectory);
        blobClient.downloadToFile(downloadZipFile);

        try (ZipFile zipFile = new ZipFile(new File(downloadZipFile))) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            List<String> fileNames = new ArrayList<>();
            while(entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                fileNames.add(entry.getName());
            }

            assertEquals(1, fileNames.size());
            assertEquals(csvFileName, fileNames.get(0));
        }
    }

}
