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
public final class UpdateProfileMessage extends Message<UpdateProfileMessageMetadata> {
    @Serial
    private static final long serialVersionUID = 0L;

    public UpdateProfileMessage(String messageId, LocalDateTime messageTimestamp, UpdateProfileMessageMetadata metadata) {
        super(messageId, messageTimestamp, metadata);
    }
}
