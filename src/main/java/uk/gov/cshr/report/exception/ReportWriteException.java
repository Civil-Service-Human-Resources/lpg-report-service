package uk.gov.cshr.report.exception;

public class ReportWriteException extends RuntimeException {
    public ReportWriteException(Throwable e) {
        super("Unable to create report", e);
    }
}
