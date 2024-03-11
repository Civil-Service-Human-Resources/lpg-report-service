package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class HttpService {
    private final RestTemplate restTemplate;
    private final ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory;

    public HttpService(RestTemplate restTemplate, ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory) {
        this.restTemplate = restTemplate;
        this.parameterizedTypeReferenceFactory = parameterizedTypeReferenceFactory;
    }

    <T> List<T> getList(URI uri, Class<T> type) {
        RequestEntity requestEntity = buildRequest(uri);
        ResponseEntity<List<T>> response = restTemplate.exchange(requestEntity,
                parameterizedTypeReferenceFactory.createListReference(type)
        );

        return response.getBody();
    }

    private RequestEntity buildRequest(URI uri) {
        return RequestEntity.get(uri).build();
    }
}
