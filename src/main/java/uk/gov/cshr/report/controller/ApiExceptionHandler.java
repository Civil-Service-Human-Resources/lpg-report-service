package uk.gov.cshr.report.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.cshr.report.exception.ProfessionNotSetException;

@ControllerAdvice
public class ApiExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ProfessionNotSetException.class)
    protected ResponseEntity handleException(ProfessionNotSetException e) {
        LOGGER.info("Bad Request: ", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        LOGGER.error("Internal Server Error: ", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
