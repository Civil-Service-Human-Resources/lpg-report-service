package uk.gov.cshr.report.service;

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

    <K,V> Map<K,V> getMap(URI uri, Class<K> keyType, Class<V> valueType) {
        RequestEntity requestEntity = buildRequest(uri);
        ResponseEntity<Map<K, V>> response = restTemplate.exchange(requestEntity,
                parameterizedTypeReferenceFactory.createMapReference(keyType, valueType)
        );

        return response.getBody();
    }

    private RequestEntity buildRequest(URI uri) {
        HttpHeaders headers = httpHeadersFactory.authorizationHeaders(accessTokenService.getAccessToken());
        return requestEntityFactory.createGetRequest(headers, uri);
    }
}
