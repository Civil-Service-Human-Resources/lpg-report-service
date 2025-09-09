package uk.gov.cshr.report.config.reports;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "report-export.course-completions")
@Getter
public class CourseCompletionsReportConfig extends ReportExportConfig {
    private final String defaultTimezone;

    public CourseCompletionsReportConfig(Integer maxRequestsPerUser, String jobCron, String tempDir, String defaultTimezone) {
        super(maxRequestsPerUser, jobCron, tempDir);
        this.defaultTimezone = defaultTimezone;
    }
}
