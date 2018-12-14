package uk.gov.cshr.report.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class RequestEntityFactory {
    RequestEntity createGetRequest(HttpHeaders headers, URI uri) {
        return new RequestEntity(headers, HttpMethod.GET, uri);
    }
}
