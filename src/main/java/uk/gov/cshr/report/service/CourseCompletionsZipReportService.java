package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionCsvDetailed;
import uk.gov.cshr.report.domain.report.CourseCompletionCsvStandard;
import uk.gov.cshr.report.domain.report.CourseCompletionCsvType;
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
    private final CourseCompletionCsvRowDetailedFactory courseCompletionCsvRowDetailedFactory;
    private final CourseCompletionCsvRowStandardFactory courseCompletionCsvRowStandardFactory;
    private final CsvService<CourseCompletionCsvDetailed> courseCompletionsCsvService;
    private final CsvService<CourseCompletionCsvStandard> courseCompletionsCsvServiceStandard;
    private final ZipService zipService;

    public CourseCompletionsZipReportService(BlobStorageService blobStorageService,
                                             CourseCompletionCsvRowDetailedFactory courseCompletionCsvRowFactory,
                                             CourseCompletionCsvRowStandardFactory courseCompletionCsvRowStandardFactory,
                                             CsvService<CourseCompletionCsvDetailed> courseCompletionsCsvService,
                                             CsvService<CourseCompletionCsvStandard> courseCompletionsCsvServiceStandard,
                                             ZipService zipService) {
        this.blobStorageService = blobStorageService;
        this.courseCompletionCsvRowDetailedFactory = courseCompletionCsvRowFactory;
        this.courseCompletionCsvRowStandardFactory = courseCompletionCsvRowStandardFactory;
        this.courseCompletionsCsvService = courseCompletionsCsvService;
        this.zipService = zipService;
        this.courseCompletionsCsvServiceStandard = courseCompletionsCsvServiceStandard;
    }

    public void createAndUploadReport(List<CourseCompletionEvent> completions, String fileName, CourseCompletionCsvType courseCompletionCsvType) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String csvFileName;
        String zipFileName;
        if(courseCompletionCsvType.equals(CourseCompletionCsvType.DETAILED)) {
            CsvData<CourseCompletionCsvDetailed> csvData = courseCompletionCsvRowDetailedFactory.getCsvData(completions);
            csvFileName = courseCompletionsCsvService.createCsvFile(csvData, fileName);
            zipFileName = zipService.createZipFile(fileName, csvFileName);
        }
        else{
            CsvData<CourseCompletionCsvStandard> csvData = courseCompletionCsvRowStandardFactory.getCsvData(completions);
            csvFileName = courseCompletionsCsvServiceStandard.createCsvFile(csvData, fileName);
            zipFileName = zipService.createZipFile(fileName, csvFileName);
        }

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
