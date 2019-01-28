package uk.gov.cshr.report.service.registry.domain;

import lombok.Data;

@Data
public class Grade {
    private long id;
    private String code;
    private String name;
    private OrganisationalUnit organisationalUnit;
}
