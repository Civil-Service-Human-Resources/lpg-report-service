package uk.gov.cshr.report.domain.learnerrecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.cshr.report.exception.UnknownStatusException;

import java.util.Arrays;

public enum BookingStatus {
    REQUESTED("Requested"), CONFIRMED("Confirmed"), CANCELLED("Cancelled");

    private String value;

    BookingStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static BookingStatus forValue(String value) {
        return Arrays.stream(BookingStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new UnknownStatusException(value));
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
