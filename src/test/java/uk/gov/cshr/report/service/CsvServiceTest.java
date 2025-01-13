package uk.gov.cshr.report.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cshr.report.domain.report.CourseCompletionCsv;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class CsvServiceTest {

    private final CsvService<CourseCompletionCsv> csvService = new CsvService<>();

    @TempDir
    private Path temporaryDir;

    @Test
    public void testContentsOfCsvFile() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        String filename = String.format("%s/testFile", temporaryDir);
        List<CourseCompletionCsv> csvRowData = List.of(
        new CourseCompletionCsv("email@email.com", "c1", "course1", LocalDateTime.of(2024, 1, 1, 10, 20, 0), 1, "Org1", 2, "Prof2", 3, "Grade3"),
        new CourseCompletionCsv("email2@email.com", "c2", "course2", LocalDateTime.of(2024, 1, 29, 11, 0, 0), 2, "Org2", 2, "Prof2", 3, "Grade3"),
        new CourseCompletionCsv("email@domain.com", "c3", "course3", LocalDateTime.of(2024, 1, 17, 23, 10, 0), 3, "Org3", 2, "Prof2", 3, "Grade3"));
        CustomColumnPositionStrategy<CourseCompletionCsv> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(CourseCompletionCsv.class);
        CsvData<CourseCompletionCsv> rawCsvData = new CsvData<>(csvRowData, strategy);

        String csvFileName = csvService.createCsvFile(rawCsvData, filename);

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFileName))) {
            List<String[]> lines = csvReader.readAll();
            String[] header = lines.remove(0);
            assertEquals("userEmail", header[0]);
            assertEquals("courseId", header[1]);
            assertEquals("courseTitle", header[2]);
            assertEquals("eventTimestamp", header[3]);
            assertEquals("organisationId", header[4]);
            assertEquals("organisationName", header[5]);
            assertEquals("professionId", header[6]);
            assertEquals("professionName", header[7]);
            assertEquals("gradeId", header[8]);
            assertEquals("gradeName", header[9]);
            String[][] rows = {
                    {"email@email.com", "c1", "course1", "2024-01-01T10:20", "1", "Org1", "2", "Prof2", "3", "Grade3"},
                    {"email2@email.com", "c2", "course2", "2024-01-29T11:00", "2", "Org2", "2", "Prof2", "3", "Grade3"},
                    {"email@domain.com", "c3", "course3", "2024-01-17T23:10", "3", "Org3", "2", "Prof2", "3", "Grade3"}
            };
            for (int i = 0; i < lines.size(); i++) {
                assertCsvRow(lines.get(i), rows[i]);
            }
        } catch (Exception e) {
            fail(String.format("Exception when reading file: %s", e));
        }
    }

    private void assertCsvRow(String[] row, String[] expectedValues) {
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], row[i]);
        }
    }
}
