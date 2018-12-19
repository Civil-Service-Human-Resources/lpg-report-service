package uk.gov.cshr.report.domain.registry;

import lombok.Data;

import java.util.List;

@Data
public class CivilServant {
    private String id;
    private String name;
    private String email;
    private String organisation;
    private String profession;
    private List<String> otherAreasOfWork;
    private String grade;
}
