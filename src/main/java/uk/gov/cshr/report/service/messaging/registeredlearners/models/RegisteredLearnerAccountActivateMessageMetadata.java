package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;

import java.io.Serial;

@Getter
public final class RegisteredLearnerAccountActivateMessageMetadata
        extends RegisteredLearnerMetadata<RegisteredLearnerAccountActivate> {
    @Serial
    private static final long serialVersionUID = 0L;
    private final RegisteredLearnerAccountActivate data;

    public RegisteredLearnerAccountActivateMessageMetadata(RegisteredLearnerAccountActivate data) {
        super(RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.ACCOUNT_ACTIVATE, data);
        this.data = data;
    }
}
