package uk.gov.cshr.report.domain.identity;

import lombok.Data;

import java.util.List;

@Data
public class Identity {
    private String username;
    private String uid;
    private List<String> Roles;
}