package uk.gov.cshr.report.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import uk.gov.cshr.report.reports.ModuleReportRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleReportRowRepository {
    private static final String GET_LEARNER_RECORD_REPORT_DATA = "select i.email, i.uid, c.full_name, ou.name, p.name, g.name, group_concat(p2.name), mr.module_id, mr.state, cr.user_id, mr.updated_at, mr.completion_date FROM learner_record.module_record mr " +
        "LEFT JOIN learner_record.course_record cr ON cr.course_id = mr.course_id " +
        "INNER JOIN identity.identity i ON cr.user_id = i.uid " +
        "INNER JOIN csrs.civil_servant c ON i.id = c.identity_id " +
        "LEFT JOIN csrs.organisational_unit ou ON ou.id = c.organisational_unit_id " +
        "LEFT JOIN csrs.profession p ON p.id = c.profession_id " +
        "LEFT JOIN csrs.grade g ON g.id = c.grade_id " +
        "LEFT JOIN csrs.civil_servant_other_areas_of_work oaw on c.id = oaw.civil_servant_id " +
        "LEFT JOIN csrs.profession p2 on p2.id  = oaw.other_areas_of_work_id " +
        "WHERE mr.updated_at BETWEEN ? AND ? AND mr.course_id IS NOT NULL " +
        "GROUP BY c.id, learner_record.mr.module_id, learner_record.mr.state, learner_record.cr.user_id, learner_record.mr.updated_at, learner_record.mr.completion_date";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ModuleReportRowRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ModuleReportRow> getModuleReportData(LocalDate date, boolean isProfessionReporter) {
        return jdbcTemplate.query(GET_LEARNER_RECORD_REPORT_DATA, (rs, rowNum) -> extractModuleReportRow(rs, isProfessionReporter), date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    private ModuleReportRow extractModuleReportRow(ResultSet rs, boolean isProfessionReporter) throws SQLException {
        ModuleReportRow reportRow = new ModuleReportRow();

        if (!isProfessionReporter) {
            reportRow.setEmail(rs.getString(1));
            reportRow.setLearnerId(rs.getString(2));
            reportRow.setName(rs.getString(3));
        }

        reportRow.setDepartment(rs.getString(4));
        reportRow.setProfession(rs.getString(5));
        reportRow.setGrade(rs.getString(6));
        reportRow.setOtherAreasOfWork(rs.getString(7));
        reportRow.setModuleId(rs.getString(8));

        String state = rs.getString(9);
        if (state != null) {
            reportRow.setStatus(state);
        }
        reportRow.setLearnerId(rs.getString(10));
        reportRow.setUpdatedAt(rs.getString(11));
        reportRow.setCompletedAt(rs.getString(12));

        return reportRow;
    }
}
