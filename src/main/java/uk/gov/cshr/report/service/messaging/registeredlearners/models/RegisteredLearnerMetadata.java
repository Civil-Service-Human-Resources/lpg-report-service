package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisteredLearnerMetadata<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private RegisteredLearnerOperation operation;
    private RegisteredLearnerDataType dataType;
    private T data;
}
