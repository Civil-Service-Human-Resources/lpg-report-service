package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.exception.MessageProcessingException;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegisteredLearnerConverterService {

    private final ObjectMapper mapper;
    private final List<RegisteredLearnerMessageService<?>> messageServices;

    public RegisteredLearnerConverterService(ObjectMapper mapper, List<RegisteredLearnerMessageService<?>> messageServices) {
        this.mapper = mapper;
        this.messageServices = messageServices;
    }

    public void processMessage(String message) {
        try {
            JsonNode messageJson = mapper.readTree(message);
            RegisteredLearnerOperation op = RegisteredLearnerOperation.valueOf(messageJson.get("operation").asText());
            RegisteredLearnerDataType dataType = RegisteredLearnerDataType.valueOf(messageJson.get("dataType").asText());
            Optional<RegisteredLearnerMessageService<?>> serviceOptional = messageServices.stream().filter(ms -> ms.matches(op, dataType)).findFirst();
            if (serviceOptional.isPresent()) {
                serviceOptional.get().process(message);
            } else {
                throw new IllegalStateException(String.format("Unexpected registered learner operation and data type combination: %s, %s", op, dataType));
            }
        } catch (Exception e) {
            log.error("Failed to process message {}. Error: {}", message, e.getMessage());
            throw new MessageProcessingException(e);
        }
    }
}
