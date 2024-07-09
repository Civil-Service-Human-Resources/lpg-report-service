package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
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
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-01-01T23:59:00",
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
                .jsonPath("$.timezone").isEqualTo("UTC")
                .jsonPath("$.delimiter").isEqualTo("hour")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(2)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T09:00:00")
                .jsonPath("$.results[1].courseId").isEqualTo("c2")
                .jsonPath("$.results[1].total").isEqualTo(1)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-01T10:00:00")
                .jsonPath("$.results[2].courseId").isEqualTo("c2")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-01-01T18:00:00")
                .jsonPath("$.results[3].courseId").isEqualTo("c1")
                .jsonPath("$.results[3].total").isEqualTo(1)
                .jsonPath("$.results[3].dateBin").isEqualTo("2024-01-01T19:00:00")
                .jsonPath("$.results[4].courseId").isEqualTo("c1")
                .jsonPath("$.results[4].total").isEqualTo(1)
                .jsonPath("$.results[4].dateBin").isEqualTo("2024-01-01T23:00:00");
    }

    @Test
    public void testGetAggregationsByDay() {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-02-04T00:00:00",
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
                .jsonPath("$.timezone").isEqualTo("UTC")
                .jsonPath("$.delimiter").isEqualTo("day")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(4)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00")
                .jsonPath("$.results[1].courseId").isEqualTo("c3")
                .jsonPath("$.results[1].total").isEqualTo(1)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-02-02T00:00:00");
    }

    @Test
    public void testGetAggregationsByDayWithTimezone() {
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
                .jsonPath("$.timezone").isEqualTo("+1")
                .jsonPath("$.delimiter").isEqualTo("day")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(3)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00")
                .jsonPath("$.results[1].courseId").isEqualTo("c1")
                .jsonPath("$.results[1].total").isEqualTo(1)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-02T00:00:00")
                .jsonPath("$.results[2].courseId").isEqualTo("c3")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-02-02T00:00:00");
    }

    @Test
    public void testGetAggregationsByWeek() {
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
                .jsonPath("$.timezone").isEqualTo("UTC")
                .jsonPath("$.delimiter").isEqualTo("week")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(3)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00")

                .jsonPath("$.results[1].courseId").isEqualTo("c2")
                .jsonPath("$.results[1].total").isEqualTo(2)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-01T00:00:00")

                .jsonPath("$.results[2].courseId").isEqualTo("c2")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-01-29T00:00:00")

                .jsonPath("$.results[3].courseId").isEqualTo("c2")
                .jsonPath("$.results[3].total").isEqualTo(1)
                .jsonPath("$.results[3].dateBin").isEqualTo("2024-02-26T00:00:00");
    }

    @Test
    public void testGetAggregationsByMonth() {
        String input = """
                {
                    "startDate": "2024-01-01T00:00:00",
                    "endDate": "2024-06-02T00:00:00",
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
                .jsonPath("$.timezone").isEqualTo("UTC")
                .jsonPath("$.delimiter").isEqualTo("month")
                .jsonPath("$.results[0].courseId").isEqualTo("c1")
                .jsonPath("$.results[0].total").isEqualTo(4)
                .jsonPath("$.results[0].dateBin").isEqualTo("2024-01-01T00:00:00")

                .jsonPath("$.results[1].courseId").isEqualTo("c2")
                .jsonPath("$.results[1].total").isEqualTo(2)
                .jsonPath("$.results[1].dateBin").isEqualTo("2024-01-01T00:00:00")

                .jsonPath("$.results[2].courseId").isEqualTo("c1")
                .jsonPath("$.results[2].total").isEqualTo(1)
                .jsonPath("$.results[2].dateBin").isEqualTo("2024-02-01T00:00:00")

                .jsonPath("$.results[3].courseId").isEqualTo("c2")
                .jsonPath("$.results[3].total").isEqualTo(2)
                .jsonPath("$.results[3].dateBin").isEqualTo("2024-02-01T00:00:00")

                .jsonPath("$.results[4].courseId").isEqualTo("c3")
                .jsonPath("$.results[4].total").isEqualTo(1)
                .jsonPath("$.results[4].dateBin").isEqualTo("2024-02-01T00:00:00")

                .jsonPath("$.results[5].courseId").isEqualTo("c5")
                .jsonPath("$.results[5].total").isEqualTo(1)
                .jsonPath("$.results[5].dateBin").isEqualTo("2024-02-01T00:00:00")

                .jsonPath("$.results[6].courseId").isEqualTo("c2")
                .jsonPath("$.results[6].total").isEqualTo(2)
                .jsonPath("$.results[6].dateBin").isEqualTo("2024-03-01T00:00:00")

                .jsonPath("$.results[7].courseId").isEqualTo("c4")
                .jsonPath("$.results[7].total").isEqualTo(1)
                .jsonPath("$.results[7].dateBin").isEqualTo("2024-03-01T00:00:00")

                .jsonPath("$.results[8].courseId").isEqualTo("c5")
                .jsonPath("$.results[8].total").isEqualTo(3)
                .jsonPath("$.results[8].dateBin").isEqualTo("2024-03-01T00:00:00")

                .jsonPath("$.results[9].courseId").isEqualTo("c5")
                .jsonPath("$.results[9].total").isEqualTo(1)
                .jsonPath("$.results[9].dateBin").isEqualTo("2024-06-01T00:00:00");
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

    @Test
    public void testPostReportRequestsReturnsAddedSuccessfullyTrueResponse(){
        String postReportRequestsEndpoint = "/course-completions/report-requests";

        String requestBody = "{\n" +
                "    \"userId\": \"testUser01\",\n" +
                "    \"userEmail\": \"user01@domain.com\",\n" +
                "    \"startDate\": \"2024-01-01\",\n" +
                "    \"endDate\": \"2024-02-01\",\n" +
                "    \"courseIds\": [\"course1\", \"course2\"],\n" +
                "    \"organisationIds\": [1,2,3,4],\n" +
                "    \"professionIds\": [5,6,7,8],\n" +
                "    \"gradeIds\": [4,3,2]\n" +
                "}";

        String expectedResponseKey = "addedSuccessfully";
        Boolean expectedResponseValue = true;

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(postReportRequestsEndpoint).build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(String.format("$.%s", expectedResponseKey)).isEqualTo(expectedResponseValue);
    }

    @Test
    public void testPostReportRequestsReturnsReturnsAddedSuccessfullyFalseResponseWithDetailsIfMaxLimitReachedForUser(){
        String postReportRequestsEndpoint = "/course-completions/report-requests";

        String requestBody = "{\n" +
                "    \"userId\": \"testUser02\",\n" +
                "    \"userEmail\": \"user02@domain.com\",\n" +
                "    \"startDate\": \"2024-01-01\",\n" +
                "    \"endDate\": \"2024-02-01\",\n" +
                "    \"courseIds\": [\"course1\", \"course2\"],\n" +
                "    \"organisationIds\": [1,2,3,4],\n" +
                "    \"professionIds\": [5,6,7,8],\n" +
                "    \"gradeIds\": [4,3,2]\n" +
                "}";

        String expectedAddedSuccessfullyResponseKey = "addedSuccessfully";
        Boolean expectedAddedSuccessfullyResponseValue = false;
        String expectedDetailsResponseKey = "details";
        String expectedDetailsResponseValue = "User has reached the maximum allowed report requests";

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(postReportRequestsEndpoint).build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(postReportRequestsEndpoint).build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath(String.format("$.%s", expectedAddedSuccessfullyResponseKey)).isEqualTo(expectedAddedSuccessfullyResponseValue)
                .jsonPath(String.format("$.%s", expectedDetailsResponseKey)).isEqualTo(expectedDetailsResponseValue);
    }

    @Test
    public void testGetReportRequestsReturnsCorrectListOfReportRequests(){
        String reportRequestsEndpoint = "/course-completions/report-requests";

        String postRequestBody = "{\n" +
                "    \"userId\": \"testUser03\",\n" +
                "    \"userEmail\": \"user03@domain.com\",\n" +
                "    \"startDate\": \"2024-01-01\",\n" +
                "    \"endDate\": \"2024-02-01\",\n" +
                "    \"courseIds\": [\"course1\", \"course2\"],\n" +
                "    \"organisationIds\": [1,2,3,4],\n" +
                "    \"professionIds\": [5,6,7,8],\n" +
                "    \"gradeIds\": [4,3,2]\n" +
                "}";

        String getRequestBody = "{\n" +
                "    \"userId\": \"testUser03\",\n" +
                "    \"status\": \"REQUESTED\"\n" +
                "}";

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(reportRequestsEndpoint).build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(postRequestBody))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        webTestClient
                .method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path(reportRequestsEndpoint).build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(getRequestBody))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.requests[0].requesterId").isEqualTo("testUser03")
                .jsonPath("$.requests[0].requesterEmail").isEqualTo("user03@domain.com")
                .jsonPath("$.requests[0].status").isEqualTo("REQUESTED")
                .jsonPath("$.requests[0].fromDate").isEqualTo("2024-01-01T00:00:00Z")
                .jsonPath("$.requests[0].toDate").isEqualTo("2024-02-01T00:00:00Z")
                .jsonPath("$.requests[0].courseIds[0]").isEqualTo("course1")
                .jsonPath("$.requests[0].courseIds[1]").isEqualTo("course2")
                .jsonPath("$.requests[0].organisationIds[0]").isEqualTo(1)
                .jsonPath("$.requests[0].organisationIds[1]").isEqualTo(2)
                .jsonPath("$.requests[0].organisationIds[2]").isEqualTo(3)
                .jsonPath("$.requests[0].organisationIds[3]").isEqualTo(4)
                .jsonPath("$.requests[0].professionIds[0]").isEqualTo(5)
                .jsonPath("$.requests[0].professionIds[1]").isEqualTo(6)
                .jsonPath("$.requests[0].professionIds[2]").isEqualTo(7)
                .jsonPath("$.requests[0].professionIds[3]").isEqualTo(8)
                .jsonPath("$.requests[0].gradeIds[0]").isEqualTo(4)
                .jsonPath("$.requests[0].gradeIds[1]").isEqualTo(3)
                .jsonPath("$.requests[0].gradeIds[2]").isEqualTo(2);
    }
}
