package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import uk.gov.cshr.report.configuration.TestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestConfig.class)
public class ApiIntegrationTest extends IntegrationTestBase {

    @Test
    public void testRemoveUserDetailsReturnsTheCorrectNumberUpdatedRows() throws Exception {
        String removeUserDetailsEndpoint = "/api/report/remove-user-details";
        String body = "{" +
                "\"uids\": [\"user1\", \"user2\"]" +
                "}";
        mockMvc.perform(put(removeUserDetailsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(getCustomAuthPostProcessor("IDENTITY_DELETE")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.deletedRegisteredLearners").value(7))
                .andExpect(jsonPath("$.updatedCourseCompletions").value(0));
    }

}
