package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import uk.gov.cshr.report.configuration.TestConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestConfig.class)
public class CourseCompletionsIntegrationTest extends IntegrationTestBase {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetAggregationsByHour() {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00Z",
                    "endDate": "2024-01-02T00:00:00Z",
                    "courseIds": ["c1", "c2"],
                    "organisationIds": [1],
                    "binDelimiter": "HOUR"
                }
                """;
        webTestClient
                .post()
                .uri("/course-completions/aggregations/by-course")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(input))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.delimiter").isEqualTo("hour")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(2)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T09:00:00Z")
                .jsonPath("$.results[1].courseId").isEqualTo("c2")
                .jsonPath("$.results[1].total").isEqualTo(1)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-01T10:00:00Z")
                .jsonPath("$.results[2].courseId").isEqualTo("c1")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-01-01T15:00:00Z")
                .jsonPath("$.results[3].courseId").isEqualTo("c2")
                .jsonPath("$.results[3].total").isEqualTo(1)
                .jsonPath("$.results[3].dateBin").isEqualTo("2024-01-01T18:00:00Z")
                .jsonPath("$.results[4].courseId").isEqualTo("c1")
                .jsonPath("$.results[4].total").isEqualTo(1)
                .jsonPath("$.results[4].dateBin").isEqualTo("2024-01-01T19:00:00Z");
    }

    @Test
    public void testGetAggregationsByDay() {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00Z",
                    "endDate": "2024-02-04T00:00:00Z",
                    "courseIds": ["c1", "c3"],
                    "organisationIds": [1],
                    "binDelimiter": "DAY"
                }
                """;
        webTestClient
                .post()
                .uri("/course-completions/aggregations/by-course")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(input))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.delimiter").isEqualTo("day")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(4)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00Z")
                .jsonPath("$.results[1].courseId").isEqualTo("c3")
                .jsonPath("$.results[1].total").isEqualTo(1)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-02-02T00:00:00Z");
    }

    @Test
    public void testGetAggregationsByWeek() {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00Z",
                    "endDate": "2024-03-21T00:00:00Z",
                    "courseIds": ["c1", "c2", "c3"],
                    "organisationIds": [1],
                    "professionIds": [2, 4],
                    "binDelimiter": "WEEK"
                }
                """;
        webTestClient
                .post()
                .uri("/course-completions/aggregations/by-course")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(input))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.delimiter").isEqualTo("week")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(3)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00Z")

                .jsonPath("$.results[1].courseId").isEqualTo("c2")
                .jsonPath("$.results[1].total").isEqualTo(2)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-01T00:00:00Z")

                .jsonPath("$.results[2].courseId").isEqualTo("c2")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-01-29T00:00:00Z")

                .jsonPath("$.results[3].courseId").isEqualTo("c2")
                .jsonPath("$.results[3].total").isEqualTo(1)
                .jsonPath("$.results[3].dateBin").isEqualTo("2024-02-26T00:00:00Z");
    }

    @Test
    public void testGetAggregationsByMonth() {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00Z",
                    "endDate": "2024-06-02T00:00:00Z",
                    "courseIds": ["c1", "c2", "c3", "c4", "c5"],
                    "organisationIds": [1, 2, 3],
                    "binDelimiter": "MONTH"
                }
                """;
        webTestClient
                .post()
                .uri("/course-completions/aggregations/by-course")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(input))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.delimiter").isEqualTo("month")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(4)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00Z")

                .jsonPath("$.results[1].courseId").isEqualTo("c2")
                .jsonPath("$.results[1].total").isEqualTo(2)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-01T00:00:00Z")

                .jsonPath("$.results[2].courseId").isEqualTo("c1")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-02-01T00:00:00Z")

                .jsonPath("$.results[3].courseId").isEqualTo("c2")
                .jsonPath("$.results[3].total").isEqualTo(2)
                .jsonPath("$.results[3].dateBin").isEqualTo("2024-02-01T00:00:00Z")

                .jsonPath("$.results[4].courseId").isEqualTo("c3")
                .jsonPath("$.results[4].total").isEqualTo(1)
                .jsonPath("$.results[4].dateBin").isEqualTo("2024-02-01T00:00:00Z")

                .jsonPath("$.results[5].courseId").isEqualTo("c5")
                .jsonPath("$.results[5].total").isEqualTo(1)
                .jsonPath("$.results[5].dateBin").isEqualTo("2024-02-01T00:00:00Z")

                .jsonPath("$.results[6].courseId").isEqualTo("c2")
                .jsonPath("$.results[6].total").isEqualTo(2)
                .jsonPath("$.results[6].dateBin").isEqualTo("2024-03-01T00:00:00Z")

                .jsonPath("$.results[7].courseId").isEqualTo("c4")
                .jsonPath("$.results[7].total").isEqualTo(1)
                .jsonPath("$.results[7].dateBin").isEqualTo("2024-03-01T00:00:00Z")

                .jsonPath("$.results[8].courseId").isEqualTo("c5")
                .jsonPath("$.results[8].total").isEqualTo(3)
                .jsonPath("$.results[8].dateBin").isEqualTo("2024-03-01T00:00:00Z")

                .jsonPath("$.results[9].courseId").isEqualTo("c5")
                .jsonPath("$.results[9].total").isEqualTo(1)
                .jsonPath("$.results[9].dateBin").isEqualTo("2024-06-01T00:00:00Z");
    }

    @Test
    public void testRemoveUserDetailsReturnsTheCorrectNumberUpdatedRows(){
        String removeUserDetailsEndpoint = "/course-completions/remove-user-details";
        String body = "{" +
                "\"uids\": [\"user1\", \"user2\"]" +
                "}";

        int expectedNumberOfUpdatedRows = 7;

        webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder.path(removeUserDetailsEndpoint).build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$").isEqualTo(expectedNumberOfUpdatedRows);
        ;
    }
}
