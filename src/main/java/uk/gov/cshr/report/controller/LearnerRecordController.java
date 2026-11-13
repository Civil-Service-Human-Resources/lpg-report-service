package uk.gov.cshr.report.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.service.LearnerRecordService;
import uk.gov.cshr.report.service.blob.DownloadableFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/learner-record")
public class LearnerRecordController {

    private final LearnerRecordService learnerRecordService;
    private final ControllerUtilities controllerUtilities;

    public LearnerRecordController(LearnerRecordService learnerRecordService, ControllerUtilities controllerUtilities) {
        this.learnerRecordService = learnerRecordService;
        this.controllerUtilities = controllerUtilities;
    }

    @GetMapping(path = {"/export", "/download"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportLearnerRecord() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.debug("Exporting learner record for user");
        DownloadableFile output = learnerRecordService.exportLearnerRecord();
        return controllerUtilities.getByteStreamResponse(output.getFileName(), output.getBytes());
    }

    @GetMapping(path = {"/export/{userId}", "/download/{userId}"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportLearnerRecord(@PathVariable("userId") String userId) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.debug("Exporting learner record for user {}", userId);
        DownloadableFile output = learnerRecordService.exportLearnerRecord(userId);
        return controllerUtilities.getByteStreamResponse(output.getFileName(), output.getBytes());
    }

    @GetMapping(produces = "text/csv")
    @RequestMapping("/events")
    public void getLearnerRecordEventsCSVReport(HttpServletResponse response)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        log.debug("Getting learner record events CSV report");

        List<LearnerRecordEvent> events = learnerRecordService.listEvents();

        try (Writer writer = response.getWriter()) {
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(events);
            response.flushBuffer();
        }
    }
}
