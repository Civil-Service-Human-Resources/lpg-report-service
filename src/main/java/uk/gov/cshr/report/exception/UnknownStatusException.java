package uk.gov.cshr.report.exception;

public class UnknownStatusException extends RuntimeException {
    public UnknownStatusException(String message) {
        super(message);
    }
}
