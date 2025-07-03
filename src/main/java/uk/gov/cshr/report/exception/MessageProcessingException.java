package uk.gov.cshr.report.exception;

public class MessageProcessingException extends RuntimeException {
    public MessageProcessingException(Throwable cause) {
        super(cause);
    }

    public MessageProcessingException(String cause) {
        super(cause);
    }
}
