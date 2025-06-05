package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.gov.cshr.report.util.WireMockServer;
import uk.gov.cshr.report.util.stub.StubService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Testcontainers
@ContextConfiguration(initializers = {IntegrationTestBase.Initializer.class})
@ActiveProfiles({"wiremock"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestBase extends WireMockServer {

    protected MockMvc mockMvc;

    @Container
    public static CustomPGContainer postgres = CustomPGContainer.getInstance();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected StubService stubService;

    protected String userUid = "userId";

    protected Jwt getJwt() {
        return new Jwt("token", Instant.now(), Instant.MAX, Map.of("alg", "none"),
                Map.of(
                        JwtClaimNames.SUB, userUid,
                        "user_name", userUid
                ));
    }

    protected SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getCustomAuthPostProcessor(String... authorities) {
        return jwt()
                .jwt(getJwt())
                .authorities(Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toArray(SimpleGrantedAuthority[]::new));
    }

    @BeforeEach
    public void setup() {
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtPostProcessor = jwt()
                .jwt(getJwt())
                .authorities(new SimpleGrantedAuthority("LEARNER"));
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(jwtPostProcessor))
                .defaultRequest(post("/").with(jwtPostProcessor))
                .defaultRequest(put("/").with(jwtPostProcessor))
                .alwaysDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .build();
    }
}
