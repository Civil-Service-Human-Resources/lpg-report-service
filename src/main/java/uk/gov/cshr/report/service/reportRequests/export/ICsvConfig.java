package uk.gov.cshr.report.service.reportRequests.export;

import org.springframework.stereotype.Component;

@Component
public interface ICsvConfig {
    String[] getColumns();
}
