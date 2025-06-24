package uk.gov.cshr.report.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.service.messaging.model.IMessageMetadata;
import uk.gov.cshr.report.service.messaging.model.Message;

@Component
@Slf4j
public class MessageConverter {

    private final ObjectMapper objectMapper;

    public MessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T extends IMessageMetadata> Message<T> convert(String message, TypeReference<Message<T>> typeReference){
        try {
            log.debug(String.format("Message received: '%s'", message));
            return objectMapper.readValue(message, typeReference);
        } catch (JsonProcessingException e) {
            log.error(String.format("Error converting message to object: %s", e));
            throw new RuntimeException(e);
        }
    }

}
