package uk.gov.cshr.report.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.reports.ModuleReportRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbRepository {
    private static final String GET_IDENTITIES = "SELECT i.email, i.uid " +
        "FROM identity.identity i";
    private static final String GET_LEARNER_RECORDS = "SELECT mr.module_id, mr.state, cr.user_id, mr.updated_at, mr.completion_date FROM module_record mr " +
        "LEFT OUTER JOIN course_record cr on ((cr.course_id, cr.user_id) = (mr.course_id, mr.user_id)) " +
        "WHERE (mr.updated_at BETWEEN ? AND ?) AND (EXISTS (select mr.course_id, mr.user_id FROM course_record cr2 where mr.course_id = cr2.course_id and mr.user_id = cr2.user_id))";
    private static final String GET_CIVIL_SERVANTS = "SELECT cr.id, cr.full_name, o.name, p.name, i.uid, g.name, group_concat(p2.name) " +
        "FROM csrs.civil_servant cr " +
        "INNER JOIN csrs.civil_servant_other_areas_of_work omw on cr.id = omw.civil_servant_id " +
        "INNER JOIN csrs.profession p2 on omw.other_areas_of_work_id = p2.id " +
        "LEFT OUTER JOIN csrs.organisational_unit o on (o.id = cr.organisational_unit_id) " +
        "LEFT OUTER JOIN csrs.profession p on (p.id = cr.profession_id) " +
        "LEFT OUTER JOIN csrs.identity i on (i.id = cr.identity_id) " +
        "LEFT OUTER JOIN csrs.grade g on (g.id = cr.grade_id) group by cr.id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Map<String, Identity> getIdentities() {
        return jdbcTemplate.query(GET_IDENTITIES, (rs, rowNum) -> extractIdentity(rs)).stream()
            .collect(Collectors.toMap(Identity::getUid, identity -> identity));
    }

    public List<ModuleRecord> getModuleRecords(LocalDate from, LocalDate to) {
        return jdbcTemplate.query(GET_LEARNER_RECORDS, (rs, rowNum) -> extractModuleRecord(rs), from, to);
    }

    public Map<String, CivilServant> getCivilServants() {
        return jdbcTemplate.query(GET_CIVIL_SERVANTS, (rs, rowNum) -> extractCivilServant(rs)).stream()
            .collect(Collectors.toMap(CivilServant::getEmail, civilServant -> civilServant));
    }

    private Identity extractIdentity(ResultSet rs) throws SQLException {
        Identity identity = new Identity();
        identity.setUsername(rs.getString(1));
        identity.setUid(rs.getString(2));

        return identity;
    }

    private ModuleRecord extractModuleRecord(ResultSet rs) throws SQLException {
        ModuleRecord moduleRecord = new ModuleRecord();
        moduleRecord.setModuleId(rs.getString(1));
        moduleRecord.setState(rs.getString(2));
        moduleRecord.setLearner(rs.getString(3));
        moduleRecord.setStateChangeDate(rs.getString(4));
        moduleRecord.setCompletedAt(rs.getString(5));

        return moduleRecord;
    }

    private CivilServant extractCivilServant(ResultSet rs) throws SQLException {
        CivilServant civilServant = new CivilServant();
        civilServant.setId(rs.getString(1));
        civilServant.setName(rs.getString(2));
        civilServant.setOrganisation(rs.getString(3));
        civilServant.setProfession(rs.getString(4));
        civilServant.setEmail(rs.getString(5));
        civilServant.setGrade(rs.getString(6));
        civilServant.setOtherAreasOfWork(rs.getString(7));

        return civilServant;
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

        String state = rs.getString(9);
        if (state != null) {
            reportRow.setStatus(state);
        }
        reportRow.setLearnerId(rs.getString(10));
        reportRow.setUpdatedAt(rs.getString(11));
        reportRow.setCompletedAt(rs.getString(12));

        reportRow.setModuleId(rs.getString(8));
        reportRow.setModuleTitle(rs.getString(13));
        reportRow.setModuleType(rs.getString(14));

        reportRow.setCourseId(rs.getString(15));
        reportRow.setCourseTitle(rs.getString(16));

        return reportRow;
    }
}
