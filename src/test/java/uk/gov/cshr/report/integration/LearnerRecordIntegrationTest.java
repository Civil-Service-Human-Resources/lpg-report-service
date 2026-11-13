package uk.gov.cshr.report.integration;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.configuration.TestConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@Transactional
public class LearnerRecordIntegrationTest extends IntegrationTestBase {

    @Test
    public void testExportLearnerRecordReturnsZipWithCsvForAuthenticatedUser() throws Exception {
        userUid = "user1";
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtPostProcessor = jwt().jwt(getJwt());

        MvcResult result = mockMvc.perform(get("/learner-record/export")
                        .with(jwtPostProcessor))
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"learner_record_2024-01-01_10-00-00.zip\""))
                .andReturn();

        byte[] responseBytes = result.getResponse().getContentAsByteArray();
        assertNotNull(responseBytes);

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(responseBytes))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals("learner_record_2024-01-01_10-00-00.csv", entry.getName());

            CSVReader reader = new CSVReader(new InputStreamReader(zis));
            List<String[]> rows = reader.readAll();

            assertEquals(4, rows.size());

            String[] header = rows.get(0);
            assertEquals("userId", header[0]);
            assertEquals("userEmail", header[1]);
            assertEquals("courseId", header[2]);
            assertEquals("courseTitle", header[3]);
            assertEquals("eventTimestamp", header[4]);
            assertEquals("organisationId", header[5]);
            assertEquals("organisationName", header[6]);
            assertEquals("professionId", header[7]);
            assertEquals("professionName", header[8]);
            assertEquals("gradeId", header[9]);
            assertEquals("gradeName", header[10]);

            for (int i = 1; i < rows.size(); i++) {
                assertEquals("user1", rows.get(i)[0]);
            }
        }
    }
}
