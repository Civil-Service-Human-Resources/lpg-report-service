package uk.gov.cshr.report.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.domain.report.CsvData;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionsDetailedCsvType;
import uk.gov.cshr.report.service.reportRequests.export.CourseCompletionsStandardCsvType;
import uk.gov.cshr.report.service.reportRequests.export.RegisteredLearnerCsvType;
import uk.gov.cshr.report.service.reportRequests.export.RegisteredLearnerDetailedCsvType;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class CsvServiceTest {

    @TempDir
    private Path temporaryDir;

    CsvService csvService = new CsvService();

    private final List<CourseCompletionEvent> courseCompletionCsvData = List.of(
        new CourseCompletionEvent("", "", "email@email.com", "c1", "course1", LocalDateTime.of(2024, 1, 1, 10, 20, 0), 1, "Org1", 2, "Prof2", 3, "Grade3"),
        new CourseCompletionEvent("", "", "email2@email.com", "c2", "course2", LocalDateTime.of(2024, 1, 29, 11, 0, 0), 2, "Org2", 2, "Prof2", 3, "Grade3"),
        new CourseCompletionEvent("", "", "email@domain.com", "c3", "course3", LocalDateTime.of(2024, 1, 17, 23, 10, 0), 3, "Org3", 2, "Prof2", 3, "Grade3")
    );

    private final List<RegisteredLearner> registeredLearnerCsvData = List.of(
            new RegisteredLearner("", "email@email.com", true, "name 1", 1, "org 1", 1, "grade1", 1, "profession1", LocalDateTime.of(2024, 1, 1, 10, 20, 0, 0), LocalDateTime.of(2024, 1, 1, 10, 20, 0, 0)),
            new RegisteredLearner("", "email2@email.com", false, "name 2", 1, "org 1", null, null, 1, "profession1", LocalDateTime.of(2024, 1, 1, 10, 20, 0, 0), LocalDateTime.of(2024, 1, 1, 10, 20, 0, 0)),
            new RegisteredLearner("", "email3@email.com", true, "name 3", 2, "org 2", 1, "grade1", 1, "profession1", LocalDateTime.of(2024, 1, 1, 10, 20, 0, 0), LocalDateTime.of(2024, 1, 1, 10, 20, 0, 0))
    );

    @Test
    public void testContentsOfRegisteredLearnerCsvFile() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        String filename = String.format("%s/testFile", temporaryDir);
        CustomColumnPositionStrategy<RegisteredLearner> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(RegisteredLearner.class);
        strategy.setColumnMapping(new RegisteredLearnerCsvType().getColumns());
        CsvData<RegisteredLearner> csvData = new CsvData<>(strategy, registeredLearnerCsvData);
        String csvFileName = csvService.createCsvFile(csvData, filename);

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFileName))) {
            List<String[]> lines = csvReader.readAll();
            String[] header = lines.remove(0);
            assertEquals("active", header[0]);
            assertEquals("gradeName", header[1]);
            assertEquals("professionName", header[2]);
            assertEquals("organisationName", header[3]);
            String[][] rows = {
                    {"true", "grade1", "profession1", "org 1"},
                    {"false", "", "profession1", "org 1",},
                    {"true", "grade1", "profession1", "org 2"}
            };
            for (int i = 0; i < lines.size(); i++) {
                assertCsvRow(lines.get(i), rows[i]);
            }
        } catch (Exception e) {
            fail(String.format("Exception when reading file: %s", e));
        }
    }

    @Test
    public void testContentsOfDetailedRegisteredLearnerCsvFile() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        String filename = String.format("%s/testFile", temporaryDir);
        CustomColumnPositionStrategy<RegisteredLearner> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(RegisteredLearner.class);
        strategy.setColumnMapping(new RegisteredLearnerDetailedCsvType().getColumns());
        CsvData<RegisteredLearner> csvData = new CsvData<>(strategy, registeredLearnerCsvData);
        String csvFileName = csvService.createCsvFile(csvData, filename);

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFileName))) {
            List<String[]> lines = csvReader.readAll();
            String[] header = lines.remove(0);
            assertEquals("active", header[0]);
            assertEquals("email", header[1]);
            assertEquals("fullName", header[2]);
            assertEquals("gradeName", header[3]);
            assertEquals("professionName", header[4]);
            assertEquals("organisationName", header[5]);
            String[][] rows = {
                    {"true", "email@email.com", "name 1", "grade1", "profession1", "org 1"},
                    {"false", "email2@email.com", "name 2", "", "profession1", "org 1",},
                    {"true", "email3@email.com", "name 3", "grade1", "profession1", "org 2"}
            };
            for (int i = 0; i < lines.size(); i++) {
                assertCsvRow(lines.get(i), rows[i]);
            }
        } catch (Exception e) {
            fail(String.format("Exception when reading file: %s", e));
        }
    }

    @Test
    public void testContentsOfDetailedCsvFile() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        String filename = String.format("%s/testFile", temporaryDir);
        CustomColumnPositionStrategy<CourseCompletionEvent> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(CourseCompletionEvent.class);
        strategy.setColumnMapping(new CourseCompletionsDetailedCsvType().getColumns());
        CsvData<CourseCompletionEvent> csvData = new CsvData<>(strategy, courseCompletionCsvData);
        String csvFileName = csvService.createCsvFile(csvData, filename);

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

    @Test
    public void testContentsOfStandardCsvFile() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        String filename = String.format("%s/testFile", temporaryDir);
        CustomColumnPositionStrategy<CourseCompletionEvent> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(CourseCompletionEvent.class);
        strategy.setColumnMapping(new CourseCompletionsStandardCsvType().getColumns());
        CsvData<CourseCompletionEvent> csvData = new CsvData<>(strategy, courseCompletionCsvData);
        String csvFileName = csvService.createCsvFile(csvData, filename);
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFileName))) {
            List<String[]> lines = csvReader.readAll();
            String[] header = lines.remove(0);
            assertEquals("courseId", header[0]);
            assertEquals("courseTitle", header[1]);
            assertEquals("eventTimestamp", header[2]);
            assertEquals("organisationId", header[3]);
            assertEquals("organisationName", header[4]);
            assertEquals("professionId", header[5]);
            assertEquals("professionName", header[6]);
            assertEquals("gradeId", header[7]);
            assertEquals("gradeName", header[8]);
            String[][] rows = {
                    {"c1", "course1", "2024-01-01T10:20", "1", "Org1", "2", "Prof2", "3", "Grade3"},
                    {"c2", "course2", "2024-01-29T11:00", "2", "Org2", "2", "Prof2", "3", "Grade3"},
                    {"c3", "course3", "2024-01-17T23:10", "3", "Org3", "2", "Prof2", "3", "Grade3"}
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
