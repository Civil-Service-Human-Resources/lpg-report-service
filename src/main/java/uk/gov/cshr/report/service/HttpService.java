package uk.gov.cshr.report.service;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
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

    public HttpService(RestTemplate restTemplate, HttpHeadersFactory httpHeadersFactory, RequestEntityFactory requestEntityFactory, AccessTokenService accessTokenService) {
        this.restTemplate = restTemplate;
        this.httpHeadersFactory = httpHeadersFactory;
        this.requestEntityFactory = requestEntityFactory;
        this.accessTokenService = accessTokenService;
    }

    <T> List<T> getList(URI uri, Class<T[]> type) {
        RequestEntity requestEntity = buildRequest(uri);
        ResponseEntity<T[]> response = restTemplate.exchange(requestEntity, type);

        return Arrays.asList(response.getBody());
    }


    <K,V> Map<K,V> getMap(URI uri) {
        RequestEntity requestEntity = buildRequest(uri);
        ResponseEntity<Map> response = restTemplate.exchange(requestEntity, Map.class);

        return response.getBody();
    }

    private RequestEntity buildRequest(URI uri) {
        HttpHeaders headers = httpHeadersFactory.authorizationHeaders(accessTokenService.getAccessToken());
        return requestEntityFactory.createGetRequest(headers, uri);
    }
}
