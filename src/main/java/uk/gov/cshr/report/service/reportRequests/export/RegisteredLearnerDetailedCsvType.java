package uk.gov.cshr.report.service.reportRequests.export;

import org.springframework.stereotype.Component;

@Component
public class RegisteredLearnerDetailedCsvType implements ICsvConfig {

    @Override
    public String[] getColumns() {
        return new String[]{
                "active",
                "email",
                "uid",
                "fullName",
                "gradeName",
                "professionName",
                "organisationName"
        };
    }
}
