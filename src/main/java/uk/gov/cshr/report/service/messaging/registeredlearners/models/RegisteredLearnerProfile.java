package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredLearnerProfile implements Serializable {
    private String uid;
    private String email;
    private String fullName;
    private Integer organisationId;
    private String organisationName;
    private Integer gradeId;
    private String gradeName;
    private Integer professionId;
    private String professionName;
}
