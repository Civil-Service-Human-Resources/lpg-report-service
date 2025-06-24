package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;

import java.io.Serial;

@Getter
public final class CompleteProfileMessage extends RegisteredLearnerMessage<RegisteredLearnerProfile> {
    @Serial
    private static final long serialVersionUID = 0L;
    private final RegisteredLearnerProfile data;

    public CompleteProfileMessage(RegisteredLearnerProfile data) {
        super(RegisteredLearnerOperation.CREATE, RegisteredLearnerDataType.LEARNER_PROFILE, data);
        this.data = data;
    }
}
