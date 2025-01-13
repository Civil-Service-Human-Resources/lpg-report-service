package uk.gov.cshr.report.service.blob;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;

@Data
@RequiredArgsConstructor
public class DownloadableFile {

    private final String fileName;
    private final ByteArrayOutputStream bytes;

}
