package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class RegisteredLearnerMetadata<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private final RegisteredLearnerOperation operation;
    private final RegisteredLearnerDataType dataType;
    private final T data;
}
