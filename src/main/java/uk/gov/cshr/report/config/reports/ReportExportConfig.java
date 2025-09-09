package uk.gov.cshr.report.config.reports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.cshr.report.service.util.TempDirectoryResource;

import java.io.IOException;

@RequiredArgsConstructor
@Getter
public class ReportExportConfig {

    private final Integer maxRequestsPerUser;
    private final String jobCron;
    private final String tempDir;

    public TempDirectoryResource getTempDirectoryResource() throws IOException {
        return new TempDirectoryResource(this.tempDir);
    }

}
