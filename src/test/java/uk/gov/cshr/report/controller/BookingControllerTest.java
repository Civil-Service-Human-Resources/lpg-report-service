package uk.gov.cshr.report.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.service.ReportService;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookingController.class)
@RunWith(SpringRunner.class)
@WithMockUser(username = "user")
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_REPORTER"})
    public void shouldReturnBookingReport() throws Exception {
        BookingReportRow reportRow = new BookingReportRow();
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
        reportRow.setLearningProvider("learning-provider");

        List<BookingReportRow> report = Lists.newArrayList(reportRow);

        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        when(reportService.buildBookingReport(eq(from), eq(to), any())).thenReturn(report);

        mockMvc.perform(
                get("/bookings")
                        .param("from", "2018-01-01")
                        .param("to", "2018-01-31")
                        .with(csrf())
                        .accept("application/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,moduleId,moduleTitle,learningProvider,required,status,paidFor")))
                .andExpect(content().string(containsString("learner-uid,\"test name\",user@example.org,\"test department\",\"profession 1\",\"profession 2, profession3\",\"test grade\",course-id,\"course title\",module-id,\"module title\",learning-provider,true,Confirmed,true")));
    }

}