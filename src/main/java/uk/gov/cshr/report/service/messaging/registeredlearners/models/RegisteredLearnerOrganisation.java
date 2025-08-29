package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RegisteredLearnerOrganisation {
    private final Long organisationalUnitId;
    private final String organisationalUnitName;
}
