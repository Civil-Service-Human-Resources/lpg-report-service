package uk.gov.cshr.report.controller;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.cshr.report.reports.CourseReportRow;
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

@WebMvcTest(CourseController.class)
@RunWith(SpringRunner.class)
@WithMockUser(username = "user")
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnCourseReport() throws Exception {
        CourseReportRow courseReportRow = new CourseReportRow();
        courseReportRow.setLearnerId("learner-uid");
        courseReportRow.setName("test name");
        courseReportRow.setEmail("user@example.org");
        courseReportRow.setDepartment("test department");
        courseReportRow.setProfession("profession 1");
        courseReportRow.setOtherAreasOfWork("profession 2, profession3");
        courseReportRow.setGrade("test grade");

        courseReportRow.setCourseId("course-id");
        courseReportRow.setCourseTitle("course title");
        courseReportRow.setCourseTopicId("course topic id");
        courseReportRow.setRequired(true);
        courseReportRow.setPaidFor(true);
        courseReportRow.setStatus("COMPLETED");
        courseReportRow.setUpdatedAt("2020-01-01T00:00:00");
        courseReportRow.setCompletedAt("2018-01-01T00:00:00");

        List<CourseReportRow> report = Lists.newArrayList(courseReportRow);

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(reportService.buildMandatoryCourseReport(any(), any(), anyBoolean())).thenReturn(report);

        mockMvc.perform(
                get("/mandatory-courses")
                        .param("from", from.format(DateTimeFormatter.ISO_DATE))
                        .param("to", to.format(DateTimeFormatter.ISO_DATE))
                        .with(csrf())
                        .accept("application/csv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("learnerId,name,email,department,profession,otherAreasOfWork,grade,courseId,courseTitle,courseTopicId,paidFor,required,status,updatedAt,completedAt")))
                .andExpect(content().string(containsString("learner-uid,\"test name\",user@example.org,\"test department\",\"profession 1\",\"profession 2, profession3\",\"test grade\",course-id,\"course title\",\"course topic id\",true,true,COMPLETED,2020-01-01T00:00:00,2018-01-01T00:00:00")));
    }
}