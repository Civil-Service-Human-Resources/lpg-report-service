package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredLearnerAccountActivate {
    private String uid;
    private Boolean active;
}
