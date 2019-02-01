package uk.gov.cshr.report.integration;

import org.junit.Before;
import org.junit.Ignore;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
public class BookingControllerIntegrationTest {
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

    @Ignore
    @Test
    @WithMockUser
    public void shouldReturnBookingReport() throws Exception {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 12, 31);

        String learnerRecordResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/learnerrecord/reporting_bookings.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9000/reporting/bookings?from=2018-01-01&to=2018-12-31"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learnerRecordResponse, MediaType.APPLICATION_JSON_UTF8));

        String registryServiceResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(registryServiceResponse, MediaType.APPLICATION_JSON_UTF8));

        String learningCatalogueResponse = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/catalogue/reporting_events.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9001/reporting/events"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(learningCatalogueResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(
                get("/bookings")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,learningProvider,required,status")));



//                .andExpect(content().string(containsString("\"8dc80f78-9a52-4c31-ac54-d280a70c18eb\",\"course-manager@domain.com\",\"Cabinet Office\",Analysis,,\"Administrative assistant\",GvFYXcr2TJm2Y1352yyI0Q,\"Beef ribs kevin filet mignon\",xeerPqMgS2yBxlVISrWHAQ,\"Pork chop turducken ball\",\"Turkey fatback spare ribs\",false,Requested,")))
//                .andExpect(content().string(containsString("\"9fbd4ae2-2db3-44c7-9544-88e80255b56e\",,,\"HM Revenue & Customs\",Communications,,\"Executive officer\",L1U3cK3GQtuf3iDg71NqJw,\"Working with budgets\",MBlZJv-ZRDCYZsCByjzRuQ,\"Working with budgets\",false,COMPLETED,2018-12-22T17:05:51,face-to-face")));
    }


}
