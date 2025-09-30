package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.report.CsvData;
import uk.gov.cshr.report.service.blob.BlobStorageService;
import uk.gov.cshr.report.service.blob.DownloadableFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class ReportExportZipReportService {

    private final BlobStorageService blobStorageService;
    private final CsvService csvService;
    private final ZipService zipService;

    public ReportExportZipReportService(BlobStorageService blobStorageService, CsvService csvService,
                                        ZipService zipService) {
        this.blobStorageService = blobStorageService;
        this.csvService = csvService;
        this.zipService = zipService;
    }

    public <T> void createAndUploadReport(CsvData<T> csvData, String blobContainer, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String csvFileName = csvService.createCsvFile(csvData, fileName);
        String zipFileName = zipService.createZipFile(fileName, csvFileName);

        blobStorageService.uploadFile(blobContainer, zipFileName);
        log.info(String.format("Deleting csv file '%s'", csvFileName));
        Files.delete(Path.of(csvFileName));
        log.info(String.format("Deleting zip file '%s'", zipFileName));
        Files.delete(Path.of(zipFileName));
    }

    public DownloadableFile fetchBlobReport(String blobContainer, String filename) {
        String zipFileName = String.format("%s.zip", filename);
        ByteArrayOutputStream byteArrayOutputStream = blobStorageService.downloadFile(blobContainer, zipFileName);
        return new DownloadableFile(zipFileName, byteArrayOutputStream);
    }

}
