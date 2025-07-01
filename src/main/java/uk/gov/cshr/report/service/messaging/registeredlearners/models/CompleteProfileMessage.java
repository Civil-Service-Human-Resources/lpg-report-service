package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import uk.gov.cshr.report.service.messaging.model.Message;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
public final class CompleteProfileMessage extends Message<CompleteProfileMessageMetadata> {
    @Serial
    private static final long serialVersionUID = 0L;
    private final CompleteProfileMessageMetadata metadata;

    public CompleteProfileMessage(String messageId, LocalDateTime messageTimestamp, CompleteProfileMessageMetadata metadata) {
        super(messageId, messageTimestamp, metadata);
        this.metadata = metadata;
    }
}
