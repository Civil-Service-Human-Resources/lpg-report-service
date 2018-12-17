package uk.gov.cshr.report.domain.learnerrecord;

public enum BookingStatus {
    CONFIRMED("Confirmed"),CANCELLED("Cancelled");

    private final String value;

    BookingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
