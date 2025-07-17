package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
public final class RegisteredLearnerEmailUpdateMessageMetadata extends
        RegisteredLearnerMetadata<RegisteredLearnerEmailUpdate> {
    @Serial
    private static final long serialVersionUID = 0L;

    public RegisteredLearnerEmailUpdateMessageMetadata(RegisteredLearnerEmailUpdate data) {
        super(RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.EMAIL_UPDATE, data);
    }
}
