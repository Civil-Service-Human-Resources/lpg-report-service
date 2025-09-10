package uk.gov.cshr.report.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.controller.model.ErrorDtoFactory;
import uk.gov.cshr.report.service.CourseCompletionService;
import uk.gov.cshr.report.service.auth.IUserAuthService;
import uk.gov.cshr.report.service.reportRequests.CourseCompletionReportRequestService;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionReportRequestProcessorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CourseCompletionsController.class, ApiExceptionHandler.class, ErrorDtoFactory.class})
@ExtendWith(SpringExtension.class)
public class CourseCompletionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCompletionService courseCompletionService;

    @MockBean
    private CourseCompletionReportRequestService courseCompletionReportRequestService;

    @MockBean
    private CourseCompletionReportRequestProcessorService courseCompletionReportRequestProcessorService;

    @MockBean
    private IUserAuthService userAuthService;

    @MockBean
    private ControllerUtilities controllerUtilities;

    @Test
    @WithMockUser(username = "user")
    public void shouldValidateParams() throws Exception {

        mockMvc.perform(
                post("/course-completions/aggregations/by-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\": \"2018-01-01T00:00:00Z\", \"endDate\": \"2018-01-01T00:00:00Z\", \"courseIds\": [], \"organisationIds\": []}")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Field courseIds is invalid: size must be between 1 and 30")));
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsEndpointReturnsOkWhenRequestBodyIsCorrect() throws Exception {
        List<String> fakeAuthorities = new ArrayList<>();
        fakeAuthorities.add("LEARNER");
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", fakeAuthorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "value1");
        Jwt jwt = new Jwt("tokenValue", null, null, headers, claims);
        when(userAuthService.getBearerTokenFromUserAuth()).thenReturn(jwt);

        String requestBody = """
                    {
                        "userId": "user003",
                        "userEmail": "learner3@domain.com",
                        "startDate": "2024-01-01T00:00:00",
                        "endDate": "2024-02-01T00:00:00",
                        "courseIds": ["course1", "course2"],
                        "organisationIds": [1,2,3,4],
                        "professionIds": [5,6,7,8],
                        "fullName": "User",
                        "timezone": "+1",
                        "downloadBaseUrl": "https://base.com"
                    }""";

        mockMvc.perform(
                post("/course-completions/report-requests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsEndpointReturnsOkWhenRequestBodyIsCorrectWithTimezone() throws Exception {
        List<String> fakeAuthorities = new ArrayList<>();
        fakeAuthorities.add("LEARNER");
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", fakeAuthorities);
        Map<String, Object> headers = new HashMap<>();
        headers.put("header1", "value1");
        Jwt jwt = new Jwt("tokenValue", null, null, headers, claims);
        when(userAuthService.getBearerTokenFromUserAuth()).thenReturn(jwt);

        String requestBody = """
                    {
                        "userId": "user003",
                        "userEmail": "learner3@domain.com",
                        "fullName": "A test user",
                        "startDate": "2024-01-01T00:00:00",
                        "endDate": "2024-02-01T00:00:00",
                        "courseIds": ["course1", "course2"],
                        "organisationIds": [1,2,3,4],
                        "professionIds": [5,6,7,8],
                        "timezone": "Europe/London",
                        "downloadBaseUrl": "https://base.com"
                    }""";

        mockMvc.perform(
                        post("/course-completions/report-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsEndpointReturnsOkWhenRequestBodyIsMissingRequiredFields() throws Exception {
        String requestBody = "{\"userEmail\": \"learner3@domain.com\", \"startDate\": \"2024-01-01T00:00:00\", \"endDate\": \"2024-02-01T00:00:00\", \"courseIds\": [\"course1\", \"course2\"], \"organisationIds\": [1,2,3,4], \"professionIds\": [5,6,7,8]}";

        mockMvc.perform(
                        post("/course-completions/report-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    public void testPostReportRequestsEndpointReturnsBadGatewayWhenNoRequestBodyIsGiven() throws Exception {
        mockMvc.perform(
                        post("/course-completions/report-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetReportRequestsEndpointReturnsOkIfCorrectRequestBodyIsGiven() throws Exception {
        mockMvc.perform(
                        get("/course-completions/report-requests?userId=user003&status=REQUESTED")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetReportRequestsEndpointReturnsBadRequestIfRequiredRequestBodyFieldIsMissing() throws Exception {
        String requestBody = """
                {
                    "userId": "user003",
                }""";
        mockMvc.perform(
                        get("/course-completions/report-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    public void testPutRemoveUserDetailsEndpointReturnsOkWhenRequestBodyIsCorrect() throws Exception {
        String requestBody = "{\"uids\": [\"user1\", \"user2\"]}";
        mockMvc.perform(
                        put("/course-completions/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void testPutRemoveUserDetailsEndpointReturnsBadRequestWhenRequestBodyIsEmpty() throws Exception {
        String requestBody = "";
        mockMvc.perform(
                        put("/course-completions/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutRemoveUserDetailsEndpointReturnsUnauthorizedWhenRequestBodyIsCorrectButRequestIsNotAuthorized() throws Exception {
        String requestBody = "{\"uids\": [\"user1\", \"user2\"]}";
        mockMvc.perform(
                        put("/course-completions/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
