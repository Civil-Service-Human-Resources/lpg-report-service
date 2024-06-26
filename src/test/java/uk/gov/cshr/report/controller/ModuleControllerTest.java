package uk.gov.cshr.report.controller;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.controller.model.ErrorDtoFactory;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.ReportService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ModuleController.class, ApiExceptionHandler.class, ErrorDtoFactory.class})
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "user")
public class ModuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnBookingReport() throws Exception {
        ModuleReportRow reportRow = new ModuleReportRow();
        reportRow.setStatus("Confirmed");
        reportRow.setLearnerId("learner-uid");
        reportRow.setName("test name");
        reportRow.setProfession("profession 1");
        reportRow.setOtherAreasOfWork("profession 2, profession3");
        reportRow.setDepartment("test department");
        reportRow.setGrade("test grade");
        reportRow.setEmail("user@example.org");
        reportRow.setCourseId("course-id");
        reportRow.setCourseTitle("course title");
        reportRow.setModuleId("module-id");
        reportRow.setModuleTitle("module title");
        reportRow.setRequired(true);
        reportRow.setPaidFor(true);
        reportRow.setStatus("COMPLETED");
        reportRow.setUpdatedAt("2018-01-01T00:00:00");

        List<ModuleReportRow> report = Lists.newArrayList(reportRow);

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(reportService.buildModuleReport(any(), any(), anyBoolean())).thenReturn(report);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .with(csrf())
                        .accept("application/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,paidFor,updatedAt")))
                .andExpect(content().string(containsString("learner-uid,\"test name\",user@example.org,\"test department\",\"profession 1\",\"profession 2, profession3\",\"test grade\",course-id,\"course title\",module-id,\"module title\",true,COMPLETED,true,2018-01-01T00:00:00")));
    }
}
