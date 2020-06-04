package uk.gov.cshr.report.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.QueryResultExtractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbRepository {
    private static final String GET_MODULE_RECORDS = "SELECT mr.module_id, mr.state, cr.user_id, mr.updated_at, mr.completion_date FROM module_record mr " +
        "LEFT OUTER JOIN learner_record.course_record cr on ((cr.course_id, cr.user_id) = (mr.course_id, mr.user_id)) " +
        "WHERE (mr.updated_at BETWEEN ? AND ?) AND (EXISTS (select mr.course_id, mr.user_id FROM course_record cr2 where mr.course_id = cr2.course_id and mr.user_id = cr2.user_id))";
    private static final String GET_BOOKINGS = "SELECT b.id, b.accessibility_options, b.booking_time, b.cancellation_reason, b.cancellation_time, b.confirmation_time, b.event_id, b.learner_id, b.po_number, b.status, b.booking_reference " +
        "FROM learner_record.booking b " +
        "WHERE b.booking_time BETWEEN ? AND ?";
    private static final String GET_IDENTITIES = "SELECT i.email, i.uid " +
        "FROM identity.identity i";
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

    public List<ModuleRecord> getModuleRecords(LocalDate from, LocalDate to) {
        return jdbcTemplate.query(GET_MODULE_RECORDS, (rs, rowNum) -> QueryResultExtractor.extractModuleRecord(rs), from, to);
    }

    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        return jdbcTemplate.query(GET_BOOKINGS, (rs, rowNum) -> QueryResultExtractor.extractBooking(rs), from, to);
    }

    public Map<String, Identity> getIdentitiesMap() {
        return jdbcTemplate.query(GET_IDENTITIES, (rs, rowNum) -> QueryResultExtractor.extractIdentity(rs)).stream()
            .collect(Collectors.toMap(Identity::getUid, identity -> identity));
    }

    public Map<String, CivilServant> getCivilServantMap() {
        return jdbcTemplate.query(GET_CIVIL_SERVANTS, (rs, rowNum) -> QueryResultExtractor.extractCivilServant(rs)).stream()
            .collect(Collectors.toMap(CivilServant::getUuid, civilServant -> civilServant));
    }
}
