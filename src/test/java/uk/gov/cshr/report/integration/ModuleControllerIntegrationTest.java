package uk.gov.cshr.report.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.service.AccessTokenService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockRestServiceServer
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ModuleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer server;

    @MockBean
    private AccessTokenService accessTokenService;

    @Before
    public void setUp() {
        when(accessTokenService.getAccessToken()).thenReturn("access-token");
    }

    @Test
    @WithMockUser
    public void shouldReturnModuleReport() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_module-records.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9000/reporting/module-records?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_modules.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9001/reporting/modules"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,date")))
                .andExpect(content().string(containsString("\"3c706a70-3fff-4e7b-ae7f-102c1d46f569\",\"Test User\",learner@domain.com,\"Department of Health & Social Care\",,\"I don't know\",,Y9RifpJhTuODVakT8FQ5iw,\"PRINCE2: practitioner\",GQZ3lcrDRIKL1YMXuvOKIQ,\"PRINCE2: practitioner\",false,REGISTERED,2019-01-22T15:26:07,face-to-face")))
                .andExpect(content().string(containsString("\"9fbd4ae2-2db3-44c7-9544-88e80255b56e\",,,\"HM Revenue & Customs\",Communications,,\"Executive officer\",L1U3cK3GQtuf3iDg71NqJw,\"Working with budgets\",MBlZJv-ZRDCYZsCByjzRuQ,\"Working with budgets\",false,COMPLETED,2018-12-22T17:05:51,face-to-face")));
    }

    @Test
    @WithMockUser(authorities = "CSHR_REPORTER")
    public void shouldReturnModuleReportByProfessionForCshrReporter() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);
        String professionId = "2";

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_module-records.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9000/reporting/module-records?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_modules.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9001/reporting/modules?professionId=" + professionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", professionId)
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,date")))
                .andExpect(content().string(containsString("\"3c706a70-3fff-4e7b-ae7f-102c1d46f569\",\"Test User\",learner@domain.com,\"Department of Health & Social Care\",,\"I don't know\",,Y9RifpJhTuODVakT8FQ5iw,\"PRINCE2: practitioner\",GQZ3lcrDRIKL1YMXuvOKIQ,\"PRINCE2: practitioner\",false,REGISTERED,2019-01-22T15:26:07,face-to-face")))
                .andExpect(content().string(containsString("\"9fbd4ae2-2db3-44c7-9544-88e80255b56e\",,,\"HM Revenue & Customs\",Communications,,\"Executive officer\",L1U3cK3GQtuf3iDg71NqJw,\"Working with budgets\",MBlZJv-ZRDCYZsCByjzRuQ,\"Working with budgets\",false,COMPLETED,2018-12-22T17:05:51,face-to-face")));
    }

    @Test
    @WithMockUser(authorities = "PROFESSION_REPORTER")
    public void shouldReturnModuleReportByProfessionForProfessionReporter() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);
        String professionId = "2";

        String civilServantProfileResponseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(civilServantProfileResponseBody, MediaType.APPLICATION_JSON_UTF8));

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_module-records.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9000/reporting/module-records?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_modules.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9001/reporting/modules?professionId=" + professionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", professionId)
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,date")))
                .andExpect(content().string(containsString("\"3c706a70-3fff-4e7b-ae7f-102c1d46f569\",\"Test User\",learner@domain.com,\"Department of Health & Social Care\",,\"I don't know\",,Y9RifpJhTuODVakT8FQ5iw,\"PRINCE2: practitioner\",GQZ3lcrDRIKL1YMXuvOKIQ,\"PRINCE2: practitioner\",false,REGISTERED,2019-01-22T15:26:07,face-to-face")))
                .andExpect(content().string(containsString("\"9fbd4ae2-2db3-44c7-9544-88e80255b56e\",,,\"HM Revenue & Customs\",Communications,,\"Executive officer\",L1U3cK3GQtuf3iDg71NqJw,\"Working with budgets\",MBlZJv-ZRDCYZsCByjzRuQ,\"Working with budgets\",false,COMPLETED,2018-12-22T17:05:51,face-to-face")));
    }

    @Test
    @WithMockUser(authorities = "PROFESSION_REPORTER")
    public void shouldReturnModuleReportByProfessionForProfessionReporter_ProfessionNotSet() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);
        String professionId = "2";

        String civilServantProfileResponseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me_no_profession.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(civilServantProfileResponseBody, MediaType.APPLICATION_JSON_UTF8));

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_module-records.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9000/reporting/module-records?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_modules.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9001/reporting/modules?professionId=" + professionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", professionId)
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("")));
    }

    @Test
    @WithMockUser(authorities = "PROFESSION_REPORTER")
    public void shouldReturnModuleReportByProfessionForProfessionReporter_DifferentProfession() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);
        String professionId = "3";

        String civilServantProfileResponseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(civilServantProfileResponseBody, MediaType.APPLICATION_JSON_UTF8));

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_module-records.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9000/reporting/module-records?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_modules.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9001/reporting/modules?professionId=" + professionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", professionId)
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(equalTo("")));
    }

    @Test
    @WithMockUser(authorities = "ORGANISATION_REPORTER")
    public void shouldReturnModuleReportByProfessionWithInvalidRole() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);
        String professionId = "2";

        String civilServantProfileResponseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(civilServantProfileResponseBody, MediaType.APPLICATION_JSON_UTF8));

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_module-records.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9000/reporting/module-records?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_modules.json").toURI())));

        server.expect(never(), requestTo("http://localhost:9001/reporting/modules?professionId=" + professionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", professionId)
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(equalTo("")));
    }

}
