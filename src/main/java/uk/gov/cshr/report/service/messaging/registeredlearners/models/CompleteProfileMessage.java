package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.cshr.report.service.messaging.model.Message;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public final class CompleteProfileMessage extends Message<CompleteProfileMessageMetadata> {
    @Serial
    private static final long serialVersionUID = 0L;

    public CompleteProfileMessage(String messageId, LocalDateTime messageTimestamp, CompleteProfileMessageMetadata metadata) {
        super(messageId, messageTimestamp, metadata);
    }
}
