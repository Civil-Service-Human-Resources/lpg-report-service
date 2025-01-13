package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import uk.gov.cshr.report.configuration.TestConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestConfig.class)
public class CourseCompletionsIntegrationTest extends IntegrationTestBase {

    @Test
    public void testGetAggregationsByHour() throws Exception {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-01-01T23:59:00",
                    "courseIds": ["c1", "c2"],
                    "organisationIds": [1],
                    "binDelimiter": "HOUR"
                }
                """;
        mockMvc.perform(post("/course-completions/aggregations/by-course")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(input))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timezone").value("UTC"))
                .andExpect(jsonPath("$.delimiter").value("hour"))
                .andExpect(jsonPath("$.results[0].courseId").value("c1"))
                .andExpect(jsonPath("$.results[0].total").value(2))
                .andExpect(jsonPath("$.results[0].dateBin").value("2024-01-01T09:00:00"))
                .andExpect(jsonPath("$.results[1].courseId").value("c2"))
                .andExpect(jsonPath("$.results[1].total").value(1))
                .andExpect(jsonPath("$.results[1].dateBin").value("2024-01-01T10:00:00"))
                .andExpect(jsonPath("$.results[2].courseId").value("c2"))
                .andExpect(jsonPath("$.results[2].total").value(1))
                .andExpect(jsonPath("$.results[2].dateBin").value("2024-01-01T18:00:00"))
                .andExpect(jsonPath("$.results[3].courseId").value("c1"))
                .andExpect(jsonPath("$.results[3].total").value(1))
                .andExpect(jsonPath("$.results[3].dateBin").value("2024-01-01T19:00:00"))
                .andExpect(jsonPath("$.results[4].courseId").value("c1"))
                .andExpect(jsonPath("$.results[4].total").value(1))
                .andExpect(jsonPath("$.results[4].dateBin").value("2024-01-01T23:00:00"));
    }

    @Test
    public void testGetAggregationsByDay() throws Exception {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-04T00:00:00",
                    "courseIds": ["c1", "c3"],
                    "organisationIds": [1],
                    "binDelimiter": "DAY"
                }
                """;
        mockMvc.perform(post("/course-completions/aggregations/by-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timezone").value("UTC"))
                .andExpect(jsonPath("$.delimiter").value("day"))
                .andExpect(jsonPath("$.results[0].courseId").value("c1"))
                .andExpect(jsonPath("$.results[0].total").value(4))
                .andExpect(jsonPath("$.results[0].dateBin").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$.results[1].courseId").value("c3"))
                .andExpect(jsonPath("$.results[1].total").value(1))
                .andExpect(jsonPath("$.results[1].dateBin").value("2024-02-02T00:00:00"));
    }

    @Test
    public void testGetAggregationsByDayWithTimezone() throws Exception {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-04T00:00:00",
                    "timezone": "+1",
                    "courseIds": ["c1", "c3"],
                    "organisationIds": [1],
                    "binDelimiter": "DAY"
                }
                """;
        mockMvc.perform(post("/course-completions/aggregations/by-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timezone").value("+01:00"))
                .andExpect(jsonPath("$.delimiter").value("day"))
                .andExpect(jsonPath("$.results[0].courseId").value("c1"))
                .andExpect(jsonPath("$.results[0].total").value(3))
                .andExpect(jsonPath("$.results[0].dateBin").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$.results[1].courseId").value("c1"))
                .andExpect(jsonPath("$.results[1].total").value(1))
                .andExpect(jsonPath("$.results[1].dateBin").value("2024-01-02T00:00:00"))
                .andExpect(jsonPath("$.results[2].courseId").value("c3"))
                .andExpect(jsonPath("$.results[2].total").value(1))
                .andExpect(jsonPath("$.results[2].dateBin").value("2024-02-02T00:00:00"));
    }

    @Test
    public void testGetAggregationsByWeek() throws Exception {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-03-21T00:00:00",
                    "courseIds": ["c1", "c2", "c3"],
                    "organisationIds": [1],
                    "professionIds": [2, 4],
                    "binDelimiter": "WEEK"
                }
                """;
        mockMvc.perform(post("/course-completions/aggregations/by-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timezone").value("UTC"))
                .andExpect(jsonPath("$.delimiter").value("week"))
                .andExpect(jsonPath("$.results[0].courseId").value("c1"))
                .andExpect(jsonPath("$.results[0].total").value(3))
                .andExpect(jsonPath("$.results[0].dateBin").value("2024-01-01T00:00:00"))

                .andExpect(jsonPath("$.results[1].courseId").value("c2"))
                .andExpect(jsonPath("$.results[1].total").value(2))
                .andExpect(jsonPath("$.results[1].dateBin").value("2024-01-01T00:00:00"))

                .andExpect(jsonPath("$.results[2].courseId").value("c2"))
                .andExpect(jsonPath("$.results[2].total").value(1))
                .andExpect(jsonPath("$.results[2].dateBin").value("2024-01-29T00:00:00"))

                .andExpect(jsonPath("$.results[3].courseId").value("c2"))
                .andExpect(jsonPath("$.results[3].total").value(1))
                .andExpect(jsonPath("$.results[3].dateBin").value("2024-02-26T00:00:00"));
    }

    @Test
    public void testGetAggregationsByMonth() throws Exception {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-06-02T00:00:00",
                    "courseIds": ["c1", "c2", "c3", "c4", "c5"],
                    "organisationIds": [1, 2, 3],
                    "binDelimiter": "MONTH"
                }
                """;
        mockMvc.perform(post("/course-completions/aggregations/by-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timezone").value("UTC"))
                .andExpect(jsonPath("$.delimiter").value("month"))
                .andExpect(jsonPath("$.results[0].courseId").value("c1"))
                .andExpect(jsonPath("$.results[0].total").value(4))
                .andExpect(jsonPath("$.results[0].dateBin").value("2024-01-01T00:00:00"))

                .andExpect(jsonPath("$.results[1].courseId").value("c2"))
                .andExpect(jsonPath("$.results[1].total").value(2))
                .andExpect(jsonPath("$.results[1].dateBin").value("2024-01-01T00:00:00"))

                .andExpect(jsonPath("$.results[2].courseId").value("c1"))
                .andExpect(jsonPath("$.results[2].total").value(1))
                .andExpect(jsonPath("$.results[2].dateBin").value("2024-02-01T00:00:00"))

                .andExpect(jsonPath("$.results[3].courseId").value("c2"))
                .andExpect(jsonPath("$.results[3].total").value(2))
                .andExpect(jsonPath("$.results[3].dateBin").value("2024-02-01T00:00:00"))

                .andExpect(jsonPath("$.results[4].courseId").value("c3"))
                .andExpect(jsonPath("$.results[4].total").value(1))
                .andExpect(jsonPath("$.results[4].dateBin").value("2024-02-01T00:00:00"))

                .andExpect(jsonPath("$.results[5].courseId").value("c5"))
                .andExpect(jsonPath("$.results[5].total").value(1))
                .andExpect(jsonPath("$.results[5].dateBin").value("2024-02-01T00:00:00"))

                .andExpect(jsonPath("$.results[6].courseId").value("c2"))
                .andExpect(jsonPath("$.results[6].total").value(2))
                .andExpect(jsonPath("$.results[6].dateBin").value("2024-03-01T00:00:00"))

                .andExpect(jsonPath("$.results[7].courseId").value("c4"))
                .andExpect(jsonPath("$.results[7].total").value(1))
                .andExpect(jsonPath("$.results[7].dateBin").value("2024-03-01T00:00:00"))

                .andExpect(jsonPath("$.results[8].courseId").value("c5"))
                .andExpect(jsonPath("$.results[8].total").value(3))
                .andExpect(jsonPath("$.results[8].dateBin").value("2024-03-01T00:00:00"))

                .andExpect(jsonPath("$.results[9].courseId").value("c5"))
                .andExpect(jsonPath("$.results[9].total").value(1))
                .andExpect(jsonPath("$.results[9].dateBin").value("2024-06-01T00:00:00"));
    }

    @Test
    public void testRemoveUserDetailsReturnsTheCorrectNumberUpdatedRows() throws Exception {
        String removeUserDetailsEndpoint = "/course-completions/remove-user-details";
        String body = "{" +
                "\"uids\": [\"user1\", \"user2\"]" +
                "}";

        int expectedNumberOfUpdatedRows = 7;

        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor identityDeletePostProcessor = jwt()
                .jwt(getJwt())
                .authorities(new SimpleGrantedAuthority("IDENTITY_DELETE"));

        mockMvc.perform(put(removeUserDetailsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(identityDeletePostProcessor))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value(expectedNumberOfUpdatedRows));
        ;
    }
}
