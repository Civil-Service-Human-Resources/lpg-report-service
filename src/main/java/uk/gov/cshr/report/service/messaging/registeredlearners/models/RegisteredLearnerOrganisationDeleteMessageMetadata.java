package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
public final class RegisteredLearnerOrganisationDeleteMessageMetadata extends
        RegisteredLearnerMetadata<RegisteredLearnerOrganisationDelete> {
    @Serial
    private static final long serialVersionUID = 0L;

    public RegisteredLearnerOrganisationDeleteMessageMetadata(RegisteredLearnerOrganisationDelete data) {
        super(RegisteredLearnerOperation.DELETE, RegisteredLearnerDataType.ORGANISATION, data);
    }
}
