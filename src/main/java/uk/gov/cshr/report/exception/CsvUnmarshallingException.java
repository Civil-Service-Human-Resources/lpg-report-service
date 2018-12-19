package uk.gov.cshr.report.exception;

public class CsvUnmarshallingException extends RuntimeException {
    public CsvUnmarshallingException(String message, Throwable e) {
        super(message, e);
    }
}
