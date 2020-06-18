package uk.gov.cshr.report.enums;

public enum ReporterRole {
    ORGANIZATION_REPORTER("ORGANISATION_REPORTER"),
    PROFESSION_REPORTER("PROFESSION_REPORTER"),
    CSHR_REPORTER("CSHR_REPORTER");

    private final String value;

    ReporterRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
