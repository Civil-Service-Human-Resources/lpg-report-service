package uk.gov.cshr.report.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.controller.model.ErrorDtoFactory;
import uk.gov.cshr.report.service.CourseCompletionService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ApiController.class, ApiExceptionHandler.class, ErrorDtoFactory.class})
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "user")
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCompletionService courseCompletionService;

    @Test
    public void testPutRemoveUserDetailsEndpointForOneUserReturnsOkWhenRequestBodyIsCorrect() throws Exception {
        String requestBody = "{\"uids\": [\"user1\"]}";

        mockMvc.perform(
                put("/api/report/remove-user-details")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPutRemoveUserDetailsEndpointForMultipleUsersReturnsOkWhenRequestBodyIsCorrect() throws Exception {
        String requestBody = "{\"uids\": [\"user1\", \"user2\"]}";

        mockMvc.perform(
                        put("/api/report/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPutRemoveUserDetailsEndpointForNoUserReturnsOkWhenRequestBodyIsCorrect() throws Exception {
        String requestBody = "{\"uids\": []}";

        mockMvc.perform(
                        put("/api/report/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPutRemoveUserDetailsEndpointForEmptyBodyReturnsOkWhenRequestBodyIsCorrect() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(
                        put("/api/report/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPutRemoveUserDetailsEndpointReturnsBadRequestWhenRequestBodyIsEmpty() throws Exception {
        String requestBody = "";

        mockMvc.perform(
                        put("/api/report/remove-user-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
