package uk.gov.cshr.report.dto.catalogue;

import lombok.Data;

@Data
public class Module {
    private String id;
    private String title;
    private String type;
    private Boolean required;
    private Course course;
}
