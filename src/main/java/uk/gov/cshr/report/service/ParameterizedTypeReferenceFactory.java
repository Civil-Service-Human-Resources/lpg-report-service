package uk.gov.cshr.report.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ParameterizedTypeReferenceFactory {

    <K, V>  ParameterizedTypeReference<Map<K,V>> createMapReference(Class<K> keyType, Class<V> valueType) {
        return new ParameterizedTypeReference<Map<K, V>>() { };
    }

    <T> ParameterizedTypeReference<List<T>> createListReference(Class<T> type) {
        return new ParameterizedTypeReference<List<T>>() { };
    }
}
