package uk.gov.cshr.report.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.controller.model.ErrorDtoFactory;
import uk.gov.cshr.report.service.LearnerRecordService;
import uk.gov.cshr.report.service.blob.DownloadableFile;

import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({LearnerRecordController.class, ApiExceptionHandler.class, ErrorDtoFactory.class})
@ExtendWith(SpringExtension.class)
public class LearnerRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LearnerRecordService learnerRecordService;

    @MockBean
    private ControllerUtilities controllerUtilities;

    @Test
    @WithMockUser(username = "user")
    public void testExportLearnerRecordWithExportPath() throws Exception {
        runTest("/export");
    }

    @Test
    @WithMockUser(username = "user")
    public void testExportLearnerRecordWithDownloadPath() throws Exception {
        runTest("/download");
    }

    private void runTest(String uriPath) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("fake-zip-data".getBytes());
        DownloadableFile downloadableFile = new DownloadableFile("learner_record.zip", baos);

        when(learnerRecordService.exportLearnerRecord()).thenReturn(downloadableFile);

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"learner_record.zip\"");

        when(controllerUtilities.getByteStreamResponse("learner_record.zip", baos))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .headers(headers)
                        .body(resource));

        mockMvc.perform(get("/learner-record" + uriPath)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"learner_record.zip\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes("fake-zip-data".getBytes()));
    }
}
