package uk.gov.cshr.report.service;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.CourseRecord;
import uk.gov.cshr.report.exception.IllegalTypeException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
    public void shouldReturnMapParameterizedTypeReferenceForCourse() {
        ParameterizedTypeReference<Map<String, Course>> parameterizedTypeReference =
                factory.createMapReference(Course.class);

        assertEquals("java.util.Map<java.lang.String, uk.gov.cshr.report.domain.catalogue.Course>",
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
    public void shouldReturnListParameterizedTypeReferenceForCourseRecord() {
        ParameterizedTypeReference<List<CourseRecord>> parameterizedTypeReference =
                factory.createListReference(CourseRecord.class);

        assertEquals("java.util.List<uk.gov.cshr.report.domain.learnerrecord.CourseRecord>",
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