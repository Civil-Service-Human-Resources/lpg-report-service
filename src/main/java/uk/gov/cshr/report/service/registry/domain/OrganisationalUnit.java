package uk.gov.cshr.report.service.registry.domain;

import lombok.Data;

import java.util.List;

@Data
public class OrganisationalUnit {
    private String code;
    private String abbreviation;
    private List<String> paymentMethods;
}
