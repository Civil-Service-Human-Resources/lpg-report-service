package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class RegisteredLearnerOrganisationUpdateMessageMetadata extends
        RegisteredLearnerMetadata<List<RegisteredLearnerOrganisationUpdate>> {
    @Serial
    private static final long serialVersionUID = 0L;

    public RegisteredLearnerOrganisationUpdateMessageMetadata(List<RegisteredLearnerOrganisationUpdate> data) {
        super(RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.ORGANISATION, data);
    }
}
