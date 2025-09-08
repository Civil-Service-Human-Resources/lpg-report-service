package uk.gov.cshr.report.config.reports;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "report-export.registered-learners")
@Getter
public class RegisteredLearnerReportConfig extends ReportExportConfig {
    public RegisteredLearnerReportConfig(Integer maxRequestsPerUser, String jobCron, String tempDir) {
        super(maxRequestsPerUser, jobCron, tempDir);
    }
}
