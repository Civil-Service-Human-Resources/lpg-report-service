package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredLearnerEmailUpdate {
    private String uid;
    private String email;
}
