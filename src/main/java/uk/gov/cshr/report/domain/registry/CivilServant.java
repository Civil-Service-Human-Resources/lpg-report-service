package uk.gov.cshr.report.domain.registry;

import lombok.Data;

@Data
public class CivilServant {
    private String id;
    private String uuid;
    private String fullName;
    private String organisation;
    private String profession;
    private String otherAreasOfWork;
    private String grade;
}
