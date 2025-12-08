package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import uk.gov.cshr.report.service.messaging.model.Message;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
public final class RegisteredLearnerOrganisationDeleteMessage
        extends Message<RegisteredLearnerOrganisationDeleteMessageMetadata> {
    @Serial
    private static final long serialVersionUID = 0L;

    public RegisteredLearnerOrganisationDeleteMessage(String messageId, LocalDateTime messageTimestamp,
                                                      RegisteredLearnerOrganisationDeleteMessageMetadata metadata) {
        super(messageId, messageTimestamp, metadata);
    }
}
