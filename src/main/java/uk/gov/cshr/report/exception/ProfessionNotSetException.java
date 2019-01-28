package uk.gov.cshr.report.exception;

public class ProfessionNotSetException extends RuntimeException {
    public ProfessionNotSetException() {
        super("User's profession is not set");
    }
}
