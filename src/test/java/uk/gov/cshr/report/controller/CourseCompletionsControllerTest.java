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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CourseCompletionsController.class,  ApiExceptionHandler.class, ErrorDtoFactory.class})
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "user")
public class CourseCompletionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCompletionService courseCompletionService;

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
                .andExpect(content().string(containsString("Field courseIds is invalid: size must be between 1 and 30")))
                .andExpect(content().string(containsString("Field organisationIds is invalid: size must be between 1 and 400")));
    }

}
