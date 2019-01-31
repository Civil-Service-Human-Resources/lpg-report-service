package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.dto.catalogue.Event;
import uk.gov.cshr.report.dto.catalogue.Module;
import uk.gov.cshr.report.dto.learnerrecord.Booking;
import uk.gov.cshr.report.dto.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.dto.registry.CivilServant;
import uk.gov.cshr.report.exception.IllegalTypeException;

import java.util.List;
import java.util.Map;

@Component
public class ParameterizedTypeReferenceFactory {
    /**
     * The maps below are a nasty hack. Unfortunately it's necessary because of the way ParameterizedTypeReference is implemented.
     * See https://stackoverflow.com/questions/21987295/using-spring-resttemplate-in-generic-method-with-generic-parameter
     */

    private final Map<String, ParameterizedTypeReference> listParameterizedTypeReferenceMap = ImmutableMap.of(
            "uk.gov.cshr.report.dto.learnerrecord.Booking", new ParameterizedTypeReference<List<Booking>>() {},
            "uk.gov.cshr.report.dto.learnerrecord.ModuleRecord", new ParameterizedTypeReference<List<ModuleRecord>>() {}
    );

    private final Map<String, ParameterizedTypeReference> mapParameterizedTypeReferenceMap = ImmutableMap.of(
            "uk.gov.cshr.report.dto.catalogue.Event", new ParameterizedTypeReference<Map<String, Event>>() {},
            "uk.gov.cshr.report.dto.catalogue.Module", new ParameterizedTypeReference<Map<String, Module>>() {},
            "uk.gov.cshr.report.dto.registry.CivilServant",
                new ParameterizedTypeReference<Map<String, CivilServant>>() {}
    );


    <T>  ParameterizedTypeReference<Map<String, T>> createMapReference(Class<T> type) {
        if (mapParameterizedTypeReferenceMap.containsKey(type.getName())) {
            return mapParameterizedTypeReferenceMap.get(type.getName());
        }

        throw new IllegalTypeException(type);
    }

    <T> ParameterizedTypeReference<List<T>> createListReference(Class<T> type) {
        if (listParameterizedTypeReferenceMap.containsKey(type.getName())) {
            return listParameterizedTypeReferenceMap.get(type.getName());
        }

        throw new IllegalTypeException(type);
    }
}
