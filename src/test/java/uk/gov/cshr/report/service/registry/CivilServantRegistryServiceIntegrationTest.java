package uk.gov.cshr.report.service.registry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import uk.gov.cshr.report.exception.ProfessionNotSetException;
import uk.gov.cshr.report.service.AccessTokenService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockRestServiceServer
public class CivilServantRegistryServiceIntegrationTest {
    private static final String ACCESS_TOKEN = "access-token";

    @Autowired
    private CivilServantRegistryService civilServantRegistryService;

    @Autowired
    private MockRestServiceServer server;

    @MockBean
    private AccessTokenService accessTokenService;

    @Test
    public void setUp() {
        when(accessTokenService.getAccessToken()).thenReturn(ACCESS_TOKEN);
    }

    @Test
    public void shouldReturnTrueIfProfessionIsSameAsOneRequested() throws IOException, URISyntaxException {
        long professionId = 2;

        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry_service_civilServants_me.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        assertTrue(civilServantRegistryService.userHasProfession(professionId));
    }

    @Test
    public void shouldReturnFalseIfProfessionDiffersFromOneRequested() throws IOException, URISyntaxException {
        long professionId = 9;

        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry_service_civilServants_me.json").toURI())));

        server.expect(once(), requestTo("http://localhost:9002/civilServants/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        assertFalse(civilServantRegistryService.userHasProfession(professionId));
    }

    @Test
    public void shouldThrowExceptionIfUserProfessionNotSet() throws IOException, URISyntaxException {
        long professionId = 9;

        String responseBody = new String(Files.readAllBytes(
                Paths.get(getClass().getResource("/fixtures/registry_service_civilServants_me_no_profession.json").toURI())));

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
}