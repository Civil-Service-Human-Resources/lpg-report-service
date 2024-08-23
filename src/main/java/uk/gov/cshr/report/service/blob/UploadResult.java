package uk.gov.cshr.report.service.blob;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@Data
@RequiredArgsConstructor
public class UploadResult {

    private final OffsetDateTime startDate;
    private final OffsetDateTime endDate;
    private final int daysToKeepLinkActive;
    private final String downloadUrl;

}
