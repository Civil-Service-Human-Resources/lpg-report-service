package uk.gov.cshr.report.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingCancellationReason {
    PAYMENT("the booking has not been paid"),
    REQUESTED("the learner has requested that the booking be cancelled"),
    BEREAVEMENT("Family bereavement"),
    ILLNESS("Illness"),
    PRIORITIES("Other work priorities");

    private final String value;

    BookingCancellationReason(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}