package uk.gov.cshr.report.service.blob;

import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class SasSignatureFactory {

    private final Clock clock;

    public SasSignatureFactory(Clock clock) {
        this.clock = clock;
    }

    public BlobServiceSasSignatureValues generateBlobServiceSasSignatureValues(Integer unitsToKeepLinkActive, ChronoUnit unit) {
        BlobContainerSasPermission sasPermissions = BlobContainerSasPermission.parse("r");
        OffsetDateTime endDate = clock.instant().atOffset(ZoneOffset.UTC).plus(unitsToKeepLinkActive, unit);
        return new BlobServiceSasSignatureValues(endDate, sasPermissions);
    }

}
