package uk.gov.cshr.report.config.reports;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.cshr.report.service.util.TempDirectoryResource;

import java.io.IOException;

@RequiredArgsConstructor
@Getter
@Valid
public class ReportExportConfig {

    private final String defaultTimezone;
    private final Integer maxRequestsPerUser;
    private final String jobCron;
    private final String tempDir;
    private final String blobContainer;

    public TempDirectoryResource getTempDirectoryResource() throws IOException {
        return new TempDirectoryResource(this.tempDir);
    }
}
