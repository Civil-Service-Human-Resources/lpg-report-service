package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
public final class RegisteredLearnerAccountActivateMessageMetadata extends
        RegisteredLearnerMetadata<RegisteredLearnerAccountActivate> {
    @Serial
    private static final long serialVersionUID = 0L;

    public RegisteredLearnerAccountActivateMessageMetadata(RegisteredLearnerAccountActivate data) {
        super(RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.ACCOUNT_ACTIVATE, data);
    }
}
