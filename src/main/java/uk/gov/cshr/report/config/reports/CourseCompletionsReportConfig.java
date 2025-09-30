package uk.gov.cshr.report.config.reports;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "report-export.course-completions")
@Getter
public class CourseCompletionsReportConfig extends ReportExportConfig {

    public CourseCompletionsReportConfig(String defaultTimezone, Integer maxRequestsPerUser, String jobCron, String tempDir,
                                         String blobContainer) {
        super(defaultTimezone, maxRequestsPerUser, jobCron, tempDir, blobContainer);
    }
}
