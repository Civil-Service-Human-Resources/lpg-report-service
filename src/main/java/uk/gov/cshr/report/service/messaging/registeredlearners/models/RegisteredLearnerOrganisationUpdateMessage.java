package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import uk.gov.cshr.report.service.messaging.model.Message;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
public final class RegisteredLearnerOrganisationUpdateMessage
    extends Message<RegisteredLearnerOrganisationUpdateMessageMetadata> {
    @Serial
    private static final long serialVersionUID = 0L;

    public RegisteredLearnerOrganisationUpdateMessage(String messageId, LocalDateTime messageTimestamp,
                                                      RegisteredLearnerOrganisationUpdateMessageMetadata metadata) {
        super(messageId, messageTimestamp, metadata);
    }
}
