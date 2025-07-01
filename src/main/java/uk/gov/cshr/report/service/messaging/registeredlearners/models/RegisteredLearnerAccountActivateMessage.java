package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import uk.gov.cshr.report.service.messaging.model.Message;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
public final class RegisteredLearnerAccountActivateMessage
        extends Message<RegisteredLearnerAccountActivateMessageMetadata> {
    @Serial
    private static final long serialVersionUID = 0L;
    private final RegisteredLearnerAccountActivateMessageMetadata metadata;

    public RegisteredLearnerAccountActivateMessage(String messageId, LocalDateTime messageTimestamp,
                                                   RegisteredLearnerAccountActivateMessageMetadata metadata) {
        super(messageId, messageTimestamp, metadata);
        this.metadata = metadata;
    }
}
