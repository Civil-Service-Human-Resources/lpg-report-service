package uk.gov.cshr.report.controller.model;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

@Component
public class ErrorDtoFactory {
    public ErrorDto create(HttpStatus httpStatus, List<String> errors) {
        errors = new ArrayList<>(errors);
        sort(errors);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(httpStatus.value());
        errorDto.setMessage(httpStatus.getReasonPhrase());
        errorDto.setErrors(new ArrayList<>(errors));
        return errorDto;
    }

    public ErrorDto createWithErrorFields(HttpStatus httpStatus, List<FieldError> errors) {
        List<String> errorList = errors.stream().map(ef -> String.format("Field %s is invalid: %s", ef.getField(), ef.getDefaultMessage())).toList();
        return create(httpStatus, errorList);
    }
}
