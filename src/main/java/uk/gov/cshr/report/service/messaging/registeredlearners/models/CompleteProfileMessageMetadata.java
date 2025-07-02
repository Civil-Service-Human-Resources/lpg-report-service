package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class CompleteProfileMessageMetadata extends RegisteredLearnerMetadata<RegisteredLearnerProfile> {
    @Serial
    private static final long serialVersionUID = 0L;

    public CompleteProfileMessageMetadata(RegisteredLearnerProfile data) {
        super(RegisteredLearnerOperation.CREATE, RegisteredLearnerDataType.LEARNER_PROFILE, data);
    }
}
