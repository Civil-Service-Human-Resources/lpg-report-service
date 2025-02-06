package uk.gov.cshr.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ZipService {

    public String createZipFile(String fileName, String[] containingFileNames) throws IOException {
        String zipFileName = String.format("%s.zip", fileName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            for(String containingFileName: containingFileNames) {
                Path containingFilePath = Path.of(containingFileName);
                zipOutputStream.putNextEntry(new ZipEntry(containingFilePath.getFileName().toString()));
                Files.copy(containingFilePath, zipOutputStream);
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            log.error(String.format("IOException encountered whilst zipping files to %s: %s", zipFileName, e));
            throw e;
        }
        return zipFileName;
    }

    public String createZipFile(String fileName, String containingFileName) throws IOException {
        return createZipFile(fileName, new String[]{containingFileName});
    }
}
