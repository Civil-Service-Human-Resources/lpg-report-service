package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredLearnerOrganisationUpdate {
    private Long organisationId;
    private String organisationName;
}
