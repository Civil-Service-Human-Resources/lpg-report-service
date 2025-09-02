package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

@Getter
@Slf4j
public abstract class RegisteredLearnerMessageService<T> {

    private final ObjectMapper objectMapper;
    private final RegisteredLearnerOperation operation;
    private final RegisteredLearnerDataType dataType;
    private final Class<T> clazz;

    protected RegisteredLearnerMessageService(ObjectMapper objectMapper, RegisteredLearnerOperation operation, RegisteredLearnerDataType dataType, Class<T> clazz) {
        this.objectMapper = objectMapper;
        this.operation = operation;
        this.dataType = dataType;
        this.clazz = clazz;
    }

    public boolean matches(RegisteredLearnerOperation operation, RegisteredLearnerDataType dataType) {
        return this.operation == operation && this.dataType == dataType;
    }

    void process(String message) throws JsonProcessingException {
        log.debug("Attempting to convert message {} to type {}", message, clazz);
        T convertedMessage = getObjectMapper().readValue(message, this.getClazz());
        processConvertedMessage(convertedMessage);
    }

    abstract void processConvertedMessage(T convertedMessage);

}
