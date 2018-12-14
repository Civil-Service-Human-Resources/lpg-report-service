package uk.gov.cshr.report.service;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class RequestEntityFactoryTest {
    private RequestEntityFactory requestEntityFactory = new RequestEntityFactory();

    @Test
    public void shouldReturnGetRequestEntity() throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        URI uri = new URI("http://example.org");
        RequestEntity request = requestEntityFactory.createGetRequest(headers, uri);

        assertEquals(headers, request.getHeaders());
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(uri, request.getUrl());
    }
}