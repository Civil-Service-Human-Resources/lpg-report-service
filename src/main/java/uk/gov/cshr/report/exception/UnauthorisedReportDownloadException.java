package uk.gov.cshr.report.exception;

public class UnauthorisedReportDownloadException extends RuntimeException {
    public UnauthorisedReportDownloadException(String message) {
        super(message);
    }
}
