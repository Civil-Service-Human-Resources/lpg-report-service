package uk.gov.cshr.report.service.auth.exception;

public class BlankBearerTokenException extends RuntimeException{
    public BlankBearerTokenException(String message){
        super(message);
    }
}
