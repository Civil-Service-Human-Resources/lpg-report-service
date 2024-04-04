package uk.gov.cshr.report.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.cshr.report.service.messaging.model.IMessageMetadata;
import uk.gov.cshr.report.service.messaging.model.Message;

@Slf4j
public abstract class ObjectMapperQueueClient <MessageMetadata extends IMessageMetadata, Entity> implements IQueueClient {

    protected final ObjectMapper objectMapper;
    protected final MessageToEntityConverter<MessageMetadata, Entity> converter;
    protected final JpaRepository<Entity, Long> repository;
    protected final TypeReference<Message<MessageMetadata>> typeReference;

    protected ObjectMapperQueueClient(ObjectMapper objectMapper, MessageToEntityConverter<MessageMetadata, Entity> converter,
                                      JpaRepository<Entity, Long> repository, TypeReference<Message<MessageMetadata>> typeReference) {
        this.objectMapper = objectMapper;
        this.converter = converter;
        this.repository = repository;
        this.typeReference = typeReference;
    }

    @Transactional
    public void convertAndSave(String message){
        try {
            log.info(String.format("Message received: '%s'", message));
            Message<MessageMetadata> parsedMessage = objectMapper.readValue(message, typeReference);
            repository.save(this.converter.convert(parsedMessage));
        } catch (JsonProcessingException e) {
            log.error(String.format("Error converting message to object: %s", e));
            throw new RuntimeException(e);
        }
    }
}
