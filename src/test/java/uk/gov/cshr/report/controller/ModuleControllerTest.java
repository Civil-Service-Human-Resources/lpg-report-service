package uk.gov.cshr.report.controller;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.exception.ProfessionNotSetException;
import uk.gov.cshr.report.reports.ModuleReportRow;
import uk.gov.cshr.report.service.ReportService;
import uk.gov.cshr.report.service.registry.CivilServantRegistryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ModuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private CivilServantRegistryService civilServantRegistryService;

    @Test
    public void shouldReturnModuleReport() throws Exception {
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
        reportRow.setStatus("COMPLETED");
        reportRow.setDate("2018-01-01T00:00:00");

        List<ModuleReportRow> report = Lists.newArrayList(reportRow);

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(reportService.buildModuleReport(any(), any())).thenReturn(report);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,date")))
                .andExpect(content().string(containsString("learner-uid,\"test name\",user@example.org,\"test department\",\"profession 1\",\"profession 2, profession3\",\"test grade\",course-id,\"course title\",module-id,\"module title\",true,COMPLETED,2018-01-01T00:00:00")));
    }


    @Test
    @WithMockUser(username = "user", authorities = {"CSHR_REPORTER"})
    public void shouldReturnModuleReportByProfession() throws Exception {

        long professionId = 2;

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
        reportRow.setStatus("COMPLETED");
        reportRow.setDate("2018-01-01T00:00:00");

        List<ModuleReportRow> report = Lists.newArrayList(reportRow);

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(reportService.buildModuleReport(from, to, professionId)).thenReturn(report);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", String.valueOf(professionId))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,date")))
                .andExpect(content().string(containsString("learner-uid,\"test name\",user@example.org,\"test department\",\"profession 1\",\"profession 2, profession3\",\"test grade\",course-id,\"course title\",module-id,\"module title\",true,COMPLETED,2018-01-01T00:00:00")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_REPORTER"})
    public void shouldReturnModuleReportByProfessionForProfessionReporter() throws Exception {

        long professionId = 2;

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
        reportRow.setStatus("COMPLETED");
        reportRow.setDate("2018-01-01T00:00:00");

        List<ModuleReportRow> report = Lists.newArrayList(reportRow);

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(civilServantRegistryService.userHasProfession(professionId)).thenReturn(true);

        when(reportService.buildModuleReport(from, to, professionId)).thenReturn(report);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", String.valueOf(professionId))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,required,status,date")))
                .andExpect(content().string(containsString("learner-uid,\"test name\",user@example.org,\"test department\",\"profession 1\",\"profession 2, profession3\",\"test grade\",course-id,\"course title\",module-id,\"module title\",true,COMPLETED,2018-01-01T00:00:00")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_REPORTER"})
    public void shouldReturn400ForProfessionReporterIfProfessionNotSet() throws Exception {

        long professionId = 2;

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        doThrow(new ProfessionNotSetException()).when(civilServantRegistryService).userHasProfession(professionId);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", String.valueOf(professionId))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_REPORTER"})
    public void shouldReturn403IfProfessionNotSameAsUserProfession() throws Exception {

        long professionId = 2;

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(civilServantRegistryService.userHasProfession(professionId)).thenReturn(false);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", String.valueOf(professionId))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isForbidden());

        verifyZeroInteractions(reportService);
    }

    @Test
    public void shouldReturn403IfUserNotProfessionReporterOrCshrReporter() throws Exception {

        long professionId = 2;

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(civilServantRegistryService.userHasProfession(professionId)).thenReturn(false);

        mockMvc.perform(
                get("/modules")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .param("professionId", String.valueOf(professionId))
                        .with(csrf())
                        .accept("text/csv"))
                .andDo(print())
                .andExpect(status().isForbidden());

        verifyZeroInteractions(reportService);
    }
}