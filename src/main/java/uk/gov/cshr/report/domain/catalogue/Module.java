package uk.gov.cshr.report.domain.catalogue;

import lombok.Data;

@Data
public class Module {
    private String id;
    private String title;
    private Boolean required;
}
