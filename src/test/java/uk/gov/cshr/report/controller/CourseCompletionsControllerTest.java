package uk.gov.cshr.report.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.controller.model.ErrorDtoFactory;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.service.CourseCompletionService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CourseCompletionsController.class, ApiExceptionHandler.class, ErrorDtoFactory.class})
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "user")
public class CourseCompletionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCompletionService courseCompletionService;

    private CourseCompletionAggregation mockCourseCompletionAggregation(String courseId, Integer count, ZonedDateTime dateBin) {
        return new CourseCompletionAggregation() {
            @Override
            public String getCourseId() {
                return courseId;
            }

            @Override
            public ZonedDateTime getdateBin() {
                return dateBin;
            }

            @Override
            public Integer getTotal() {
                return count;
            }
        };
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldValidateParams() throws Exception {

        when(courseCompletionService.getCourseCompletions(any())).thenReturn(List.of(
                mockCourseCompletionAggregation("c1", 1, ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 1, 1), ZoneId.systemDefault()))
        ));

        mockMvc.perform(
                get("/course-completions/aggregations/by-course")
                        .param("startDate", "2018-01-")
                        .param("endDate", "2018-01-")
                        .with(csrf())
                        .accept("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Field courseIds is invalid: must not be null")))
                .andExpect(content().string(containsString("Field organisationIds is invalid: must not be null")))
                .andExpect(content().string(containsString("Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'endDate'")))
                .andExpect(content().string(containsString("Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'startDate'")));
    }

}
