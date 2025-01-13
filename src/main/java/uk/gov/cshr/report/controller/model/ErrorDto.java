package uk.gov.cshr.report.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorDto {
    private Instant timestamp = Instant.now();
    private List<String> errors = new ArrayList<>();
    private int status;
    private String message;

    @JsonIgnore
    public ResponseEntity<ErrorDto> getAsResponseEntity() {
        return new ResponseEntity<>(this, HttpStatusCode.valueOf(getStatus()));
    }
}
