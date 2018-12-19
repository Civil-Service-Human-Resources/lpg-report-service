package uk.gov.cshr.report.exception;

public class IllegalTypeException extends RuntimeException {
    public IllegalTypeException(Class type) {
        super(String.format("Unrecognized type: %s", type.getName()));
    }
}
