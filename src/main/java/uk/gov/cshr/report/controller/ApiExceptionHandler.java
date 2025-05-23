package uk.gov.cshr.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.cshr.report.controller.model.ErrorDto;
import uk.gov.cshr.report.controller.model.ErrorDtoFactory;
import uk.gov.cshr.report.exception.GenericNotFoundException;
import uk.gov.cshr.report.exception.UnauthorisedReportDownloadException;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorDtoFactory errorDtoFactory;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        return new ResponseEntity<>(errorDtoFactory.createWithErrorFields(HttpStatus.BAD_REQUEST, result.getFieldErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        log.error("Internal Server Error: ", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(UnauthorisedReportDownloadException.class)
    protected ResponseEntity<ErrorDto> handleUnathorisedReportDownloadException(UnauthorisedReportDownloadException e) {
        return errorDtoFactory.create(
                HttpStatus.FORBIDDEN, List.of("User is not authorised to download this report")
        ).getAsResponseEntity();
    }

    @ExceptionHandler(GenericNotFoundException.class)
    protected ResponseEntity<ErrorDto> handleGenericNotFoundException(GenericNotFoundException e) {
        return errorDtoFactory.create(
                HttpStatus.NOT_FOUND, List.of("The requested resource was not found")
        ).getAsResponseEntity();
    }
}
