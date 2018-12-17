package uk.gov.cshr.report.service;

import org.junit.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.Assert.assertEquals;

public class HttpHeadersFactoryTest {
    private HttpHeadersFactory httpHeadersFactory = new HttpHeadersFactory();

    @Test
    public void shouldAddTokenToAuthorizationHeader() {
        String token = "token-value";
        HttpHeaders headers = httpHeadersFactory.authorizationHeaders(token);
        assertEquals("Bearer " + token, headers.get("Authorization").get(0));
    }
}