package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredLearnerOrganisationDelete {
    private List<Long> organisationIds;
}
