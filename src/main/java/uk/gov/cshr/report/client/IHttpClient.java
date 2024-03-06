package uk.gov.cshr.report.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;

import java.util.List;
import java.util.Map;

public interface IHttpClient {
    <T, R> T executeRequest(RequestEntity<R> request, Class<T> responseClass);
    <T, R> Map<String, T> executeRequest(RequestEntity<R> request, ParameterizedTypeReference<Map<String, T>> parameterizedTypeReference);
}
