package uk.gov.cshr.report.service;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.exception.IllegalTypeException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterizedTypeReferenceFactoryTest {

    private ParameterizedTypeReferenceFactory factory = new ParameterizedTypeReferenceFactory();

    @Test
    public void shouldReturnMapParameterizedTypeReference() {
        ParameterizedTypeReference<Map<String, Event>> parameterizedTypeReference =
                factory.createMapReference(Event.class);

        assertEquals("java.util.Map<java.lang.String, uk.gov.cshr.report.domain.catalogue.Event>",
                parameterizedTypeReference.getType().toString());
    }

    @Test
    public void shouldReturnListParameterizedTypeReference() {
        ParameterizedTypeReference<List<Booking>> parameterizedTypeReference =
                factory.createListReference(Booking.class);

        assertEquals("java.util.List<uk.gov.cshr.report.domain.learnerrecord.Booking>",
                parameterizedTypeReference.getType().toString());
    }


    @Test
    public void shouldThrowIllegalTypeExceptionIfUnknownTypeInListReference() {
        try {
            factory.createListReference(Event.class);
        } catch (IllegalTypeException e) {
            assertEquals("Unrecognized type: uk.gov.cshr.report.domain.catalogue.Event", e.getMessage());
        }
    }

    @Test
    public void shouldThrowIllegalTypeExceptionIfUnknownTypeInMapReference() {
        try {
            factory.createMapReference(Booking.class);
        } catch (IllegalTypeException e) {
            assertEquals("Unrecognized type: uk.gov.cshr.report.domain.learnerrecord.Booking", e.getMessage());
        }
    }

}