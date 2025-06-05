package uk.gov.cshr.report.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import uk.gov.cshr.report.configuration.TestConfig;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;

import java.util.List;
import java.util.Map;

import static java.util.stream.StreamSupport.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestConfig.class)
public class RegisteredLearnersIntegrationTest extends IntegrationTestBase {

    @Autowired
    private RegisteredLearnerRepository registeredLearnerRepository;

    @Test
    @Transactional
    public void testDeactivateUsers() throws Exception {
        String input = """
                {
                    "uids": [
                        "uid10000-0000-0000-0000-000000000000",
                        "uid20000-0000-0000-0000-000000000000",
                        "uid30000-0000-0000-0000-000000000000"
                    ]
                }
                """;
        mockMvc.perform(put("/registered-learners/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                        .with(getCustomAuthPostProcessor("IDENTITY_MANAGE_IDENTITY")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.affectedRows").value("3"));

        assertEquals(3, stream(registeredLearnerRepository.findAll().spliterator(), false).filter(rl -> !rl.isActive()).count());
    }

    @Test
    @Transactional
    public void testDeleteUsers() throws Exception {
        List<String> uids = List.of("uid10000-0000-0000-0000-000000000000",
                "uid20000-0000-0000-0000-000000000000",
                "uid30000-0000-0000-0000-000000000000");
        String input = new ObjectMapper().writeValueAsString(Map.of("uids", uids));
        mockMvc.perform(delete("/registered-learners/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                        .with(getCustomAuthPostProcessor("IDENTITY_DELETE")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.affectedRows").value("3"));
        assertEquals(0, stream(registeredLearnerRepository.findAllById(uids).spliterator(), false).count());
    }
}
