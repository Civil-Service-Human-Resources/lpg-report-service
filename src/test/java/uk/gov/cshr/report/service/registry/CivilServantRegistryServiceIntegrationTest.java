package uk.gov.cshr.report.service.registry;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.client.MockRestServiceServer;
import uk.gov.cshr.report.dto.registry.CivilServant;
import uk.gov.cshr.report.exception.ProfessionNotSetException;
import uk.gov.cshr.report.service.AccessTokenService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(JUnitParamsRunner.class)
@AutoConfigureMockRestServiceServer
@SpringBootTest
public class CivilServantRegistryServiceIntegrationTest {
        private static final String ACCESS_TOKEN = "access-token";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private CivilServantRegistryService civilServantRegistryService;

    @Autowired
    private MockRestServiceServer server;

    @MockBean
    private AccessTokenService accessTokenService;

    @Before
    public void setUp() {
        when(accessTokenService.getAccessToken()).thenReturn(ACCESS_TOKEN);
    }

    @Test
    public void shouldReturnTrueIfProfessionIsSameAsOneRequested() throws IOException, URISyntaxException {
        long professionId = 2;

        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        assertTrue(civilServantRegistryService.userHasProfession(professionId));
    }

    @Test
    public void shouldReturnFalseIfProfessionDiffersFromOneRequested() throws IOException, URISyntaxException {
        long professionId = 9;

        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        assertFalse(civilServantRegistryService.userHasProfession(professionId));
    }

    @Test
    public void shouldThrowExceptionIfUserProfessionNotSet() throws IOException, URISyntaxException {
        long professionId = 9;

        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/civilServants_me_no_profession.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        try {
            civilServantRegistryService.userHasProfession(professionId);
            fail("Expected ProfessionNotSetException");
        } catch (ProfessionNotSetException e) {
            assertEquals("User's profession is not set", e.getMessage());
        }
    }

    @Test
    @Parameters
    public void shouldReturnMapOfUidAndCivilServantDto(String id, String name, String email, String organisation,
                                                       String profession, List<String> otherAreasOfWork, String grade) throws URISyntaxException, IOException {
        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry/report_civilServants.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/report/civilServants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        Map<String, CivilServant> civilServantMap = civilServantRegistryService.getCivilServantMap();

        CivilServant civilServant = civilServantMap.get(id);

        assertEquals(id, civilServant.getId());
        assertEquals(name, civilServant.getName());
        assertEquals(email, civilServant.getEmail());
        assertEquals(organisation, civilServant.getOrganisation());
        assertEquals(profession, civilServant.getProfession());
        assertEquals(otherAreasOfWork, civilServant.getOtherAreasOfWork());
        assertEquals(grade, civilServant.getGrade());
    }

    private Iterable<Object[]> parametersForShouldReturnMapOfUidAndCivilServantDto() {
        return Arrays.asList(new Object[][]{
                {"3c706a70-3fff-4e7b-ae7f-102c1d46f569", "Test User", "learner@domain.com", "Department of Health & Social Care", null,
                        Collections.singletonList("I don't know"), null},
                {"9fbd4ae2-2db3-44c7-9544-88e80255b56e", null, null, "HM Revenue & Customs", "Communications", Collections.emptyList(), "Executive officer"},
                {"c4cb1208-eca7-46a6-b496-0f6f354c6eac", null, "cshr-reporter@domain.com", null, "Corporate finance", Collections.emptyList(), "Grade 6"},
                {"fbc29c68-f787-45bc-acfa-d03d99a9dff0", null, null, null, null, Collections.emptyList(), null},
                {"8dc80f78-9a52-4c31-ac54-d280a70c18eb", null, "course-manager@domain.com", "Cabinet Office", "Analysis", Collections.emptyList(), "Administrative assistant" }
        });
    }
}