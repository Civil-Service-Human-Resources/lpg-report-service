package uk.gov.cshr.report.domain.registry;

import lombok.Data;

@Data
public class CivilServant {
    private String id;
    private String uuid;
    private String name;
    private String organisation;
    private String profession;
    private String otherAreasOfWork;
    private String grade;
    private String email;
}
