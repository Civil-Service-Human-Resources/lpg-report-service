package uk.gov.cshr.report.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.service.LearnerRecordService;
import uk.gov.cshr.report.service.blob.DownloadableFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Controller
@RequestMapping("/learner-record")
public class LearnerRecordController {

    private static final Logger LOG = LoggerFactory.getLogger(LearnerRecordController.class);

    private final LearnerRecordService learnerRecordService;
    private final ControllerUtilities controllerUtilities;

    public LearnerRecordController(LearnerRecordService learnerRecordService, ControllerUtilities controllerUtilities) {
        this.learnerRecordService = learnerRecordService;
        this.controllerUtilities = controllerUtilities;
    }

    @GetMapping(path = {"/export", "/download"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportLearnerRecord() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        LOG.debug("Exporting learner record for user");
        DownloadableFile output = learnerRecordService.exportLearnerRecord();
        return controllerUtilities.getByteStreamResponse(output.getFileName(), output.getBytes());
    }

    @GetMapping(produces = "text/csv")
    @RequestMapping("/events")
    public void getLearnerRecordEventsCSVReport(HttpServletResponse response)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        LOG.debug("Getting learner record events CSV report");

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
