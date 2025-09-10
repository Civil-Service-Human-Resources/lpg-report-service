package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.configuration.TestConfig;
import uk.gov.cshr.report.repository.RegisteredLearnerReportRequestRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@Transactional
public class RegisteredLearnerReportRequestIntegrationTest extends IntegrationTestBase {

    @Autowired
    private RegisteredLearnerReportRequestRepository registeredLearnerReportRequestRepository;

    @BeforeEach
    public void setUp(){
        registeredLearnerReportRequestRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsReturnsAddedSuccessfullyTrueResponse() throws Exception {
        String postReportRequestsEndpoint = "/registered-learners/report-requests";

        String requestBody = """
                {
                    "userId": "testUser01",
                    "userEmail": "user01@domain.com",
                    "fullName": "A test user",
                    "organisationIds": [1,2,3,4],
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

        String postReportRequestsEndpoint = "/registered-learners/report-requests";

        String requestBody = """
                {
                    "userId": "testUser02",
                    "userEmail": "user02@domain.com",
                    "fullName": "A test user",
                    "organisationIds": [1,2,3,4],
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

        String reportRequestsEndpoint = "/registered-learners/report-requests";

        String postRequestBody = """
                {
                    "userId": "testUser03",
                    "userEmail": "user03@domain.com",
                    "fullName": "A test user",
                    "organisationIds": [1,2,3,4],
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
                .andExpect(jsonPath("$.requests[0].organisationIds[0]").value(1))
                .andExpect(jsonPath("$.requests[0].organisationIds[1]").value(2))
                .andExpect(jsonPath("$.requests[0].organisationIds[2]").value(3))
                .andExpect(jsonPath("$.requests[0].organisationIds[3]").value(4));
    }

}
