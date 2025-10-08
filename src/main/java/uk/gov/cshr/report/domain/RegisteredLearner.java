package uk.gov.cshr.report.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uk.gov.cshr.report.domain.report.ReportableData;

import java.time.LocalDateTime;

@Entity
@Table(name = "registered_learners")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredLearner extends ReportableData {

    @Id
    @Column(length = 36, nullable = false)
    @Length(min = 36, max = 36)
    private String uid;

    @Column(length = 150, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private String fullName;

    @Column
    private Integer organisationId;

    @Column(columnDefinition = "text")
    private String organisationName;

    @Column
    private Integer gradeId;

    @Column
    private String gradeName;

    @Column
    private Integer professionId;

    @Column
    private String professionName;

    @Column(nullable = false)
    private LocalDateTime createdTimestamp;

    @Column(nullable = false)
    private LocalDateTime updatedTimestamp;

}
