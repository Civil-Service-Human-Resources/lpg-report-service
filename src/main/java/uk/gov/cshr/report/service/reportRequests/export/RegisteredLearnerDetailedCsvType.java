package uk.gov.cshr.report.service.reportRequests.export;

import org.springframework.stereotype.Component;

@Component
public class RegisteredLearnerDetailedCsvType implements ICsvConfig {

    @Override
    public String[] getColumns() {
        return new String[]{
                "active",
                "email",
                "fullName",
                "gradeName",
                "professionName",
                "organisationName"
        };
    }
}
