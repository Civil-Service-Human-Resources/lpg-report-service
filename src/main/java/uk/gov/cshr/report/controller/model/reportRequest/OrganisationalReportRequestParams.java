package uk.gov.cshr.report.controller.model.reportRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrganisationalReportRequestParams extends ReportRequestParams {

    private List<Integer> organisationIds;

}
