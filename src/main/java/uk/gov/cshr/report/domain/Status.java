package uk.gov.cshr.report.domain;

public enum Status {

    IN_PROGRESS("IN_PROGRESS");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}