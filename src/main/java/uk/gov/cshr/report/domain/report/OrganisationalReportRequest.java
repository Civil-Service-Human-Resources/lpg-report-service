package uk.gov.cshr.report.domain.report;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class OrganisationalReportRequest extends ReportRequest {

    @Type(ListArrayType.class)
    @Column(name = "organisation_ids", columnDefinition = "int[]")
    private List<Integer> organisationIds;

    public OrganisationalReportRequest(String requesterId, String requesterEmail, LocalDateTime requestedTimestamp,
                                       ReportRequestStatus status, String fullName, String requesterTimezone, String urlSlug,
                                       String downloadBaseUrl, List<Integer> organisationIds, boolean detailedExport) {
        super(requesterId, requesterEmail, requestedTimestamp, status, fullName, requesterTimezone, urlSlug, downloadBaseUrl, detailedExport);
        this.organisationIds = organisationIds;
    }
}
