package uk.gov.cshr.report.service.registry.domain;

import lombok.Data;

import java.util.Set;

@Data
public class CivilServantResource {
    private String fullName;
    private Grade grade;
    private OrganisationalUnit organisationalUnit;
    private Profession profession;
    private Set<Profession> otherAreasOfWork;
    private Set<Interest> interests;
    private String lineManagerName;
    private String lineManagerEmailAddress;
}
