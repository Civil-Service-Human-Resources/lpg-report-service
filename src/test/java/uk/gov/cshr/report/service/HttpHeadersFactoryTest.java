package uk.gov.cshr.report.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpHeadersFactoryTest {
    private HttpHeadersFactory httpHeadersFactory = new HttpHeadersFactory();

    @Test
    public void shouldAddTokenToAuthorizationHeader() {
        String token = "token-value";
        HttpHeaders headers = httpHeadersFactory.authorizationHeaders(token);
        assertEquals("Bearer " + token, headers.get("Authorization").get(0));
    }
}