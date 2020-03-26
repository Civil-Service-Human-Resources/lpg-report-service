package uk.gov.cshr.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class HttpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);
    private final RestTemplate restTemplate;
    private final HttpHeadersFactory httpHeadersFactory;
    private final RequestEntityFactory requestEntityFactory;
    private final AccessTokenService accessTokenService;
    private final ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory;

    public HttpService(RestTemplate restTemplate, HttpHeadersFactory httpHeadersFactory, RequestEntityFactory requestEntityFactory, AccessTokenService accessTokenService, ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory) {
        this.restTemplate = restTemplate;
        this.httpHeadersFactory = httpHeadersFactory;
        this.requestEntityFactory = requestEntityFactory;
        this.accessTokenService = accessTokenService;
        this.parameterizedTypeReferenceFactory = parameterizedTypeReferenceFactory;
    }

    <T> List<T> getList(URI uri, Class<T> type) {
        RequestEntity requestEntity = buildRequest(uri);
        ResponseEntity<List<T>> response = restTemplate.exchange(requestEntity,
                parameterizedTypeReferenceFactory.createListReference(type)
        );

        return response.getBody();
    }

    <T> Map<String, T> getMap(URI uri, Class<T> type) {
        RequestEntity requestEntity = buildRequest(uri);
        LOGGER.debug(String.format("GET %s", uri));
        ResponseEntity<Map<String, T>> response = restTemplate.exchange(requestEntity,
                parameterizedTypeReferenceFactory.createMapReference(type)
        );

        return response.getBody();
    }

    private RequestEntity buildRequest(URI uri) {
        HttpHeaders headers = httpHeadersFactory.authorizationHeaders(accessTokenService.getAccessToken());
        return requestEntityFactory.createGetRequest(headers, uri);
    }
}
