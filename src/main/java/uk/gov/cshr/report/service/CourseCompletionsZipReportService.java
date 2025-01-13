package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionCsv;
import uk.gov.cshr.report.service.blob.BlobStorageService;
import uk.gov.cshr.report.service.blob.DownloadableFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class CourseCompletionsZipReportService {

    private final BlobStorageService blobStorageService;
    private final CourseCompletionCsvRowFactory courseCompletionCsvRowFactory;
    private final CsvService<CourseCompletionCsv> courseCompletionsCsvService;
    private final ZipService zipService;

    public CourseCompletionsZipReportService(BlobStorageService blobStorageService,
                                             CourseCompletionCsvRowFactory courseCompletionCsvRowFactory,
                                             CsvService<CourseCompletionCsv> courseCompletionsCsvService,
                                             ZipService zipService) {
        this.blobStorageService = blobStorageService;
        this.courseCompletionCsvRowFactory = courseCompletionCsvRowFactory;
        this.courseCompletionsCsvService = courseCompletionsCsvService;
        this.zipService = zipService;
    }

    public void createAndUploadReport(List<CourseCompletionEvent> completions, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        CsvData<CourseCompletionCsv> csvData = courseCompletionCsvRowFactory.getCsvData(completions);
        String csvFileName = courseCompletionsCsvService.createCsvFile(csvData, fileName);
        String zipFileName = zipService.createZipFile(fileName, csvFileName);
        blobStorageService.uploadFile(zipFileName);
        log.info(String.format("Deleting csv file '%s'", csvFileName));
        Files.delete(Path.of(csvFileName));
        log.info(String.format("Deleting zip file '%s'", zipFileName));
        Files.delete(Path.of(zipFileName));
    }

    public DownloadableFile fetchBlobReport(String filename) {
        String zipFileName = String.format("%s.zip", filename);
        ByteArrayOutputStream byteArrayOutputStream = blobStorageService.downloadFile(zipFileName);
        return new DownloadableFile(zipFileName, byteArrayOutputStream);
    }

}
