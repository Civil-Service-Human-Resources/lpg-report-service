package uk.gov.cshr.report.factory;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.LearningProvider;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReportRowFactoryTest {

    private ReportRowFactory reportRowFactory = new ReportRowFactory();

    @Test
    public void shouldReturnBookingReportRowWithLearningProvider() {
        BookingStatus status = BookingStatus.CONFIRMED;
        String learnerUid = "learner-uid";
        String eventUid = "event-uid";
        String name = "learner name";
        String profession = "profession1";
        String otherAreasOfWork = "commercial, digital";
        String organisation = "_department";
        String grade = "_grade";
        String email = "user@example.org";
        String courseId = "course-id";
        String courseTitle = "course-title";
        String moduleId = "module-id";
        String moduleTitle = "module-title";
        String learningProviderId = "learning-provider-id";
        String learningProviderName = "learning-provider-name";

        Booking booking = new Booking();
        booking.setStatus(status);
        booking.setLearner(learnerUid);
        booking.setEvent(eventUid);
        booking.setLearnerEmail(email);

        CivilServant civilServant = new CivilServant();
        civilServant.setId(learnerUid);
        civilServant.setName(name);
        civilServant.setProfession(profession);
        civilServant.setOtherAreasOfWork(otherAreasOfWork);
        civilServant.setOrganisation(organisation);
        civilServant.setGrade(grade);

        Course course = new Course();
        course.setId(courseId);
        course.setTitle(courseTitle);

        Module module = new Module();
        module.setId(moduleId);
        module.setTitle(moduleTitle);
        module.setRequired(true);
        module.setAssociatedLearning(true);
        module.setCourse(course);

        LearningProvider learningProvider = new LearningProvider();
        learningProvider.setId(learningProviderId);
        learningProvider.setName(learningProviderName);

        Event event = new Event();
        event.setId(eventUid);
        event.setModule(module);
        event.setLearningProvider(learningProvider);

        Identity identity = new Identity();
        identity.setUsername(email);
        identity.setUid(learnerUid);

        BookingReportRow reportRow = reportRowFactory.createBookingReportRow(Optional.of(civilServant), Optional.of(event), booking, identity, false);

        assertEquals(learnerUid, reportRow.getLearnerId());
        assertEquals(name, reportRow.getName());
        assertEquals(profession, reportRow.getProfession());
        assertEquals(String.join(", ", otherAreasOfWork), reportRow.getOtherAreasOfWork());
        assertEquals(organisation, reportRow.getDepartment());
        assertEquals(grade, reportRow.getGrade());
        assertEquals(email, reportRow.getEmail());
        assertEquals(courseId, reportRow.getCourseId());
        assertEquals(courseTitle, reportRow.getCourseTitle());
        assertEquals(moduleId, reportRow.getModuleId());
        assertEquals(moduleTitle, reportRow.getModuleTitle());
        assertEquals(learningProviderName, reportRow.getLearningProvider());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"PROFESSION_AUTHOR"})
    public void shouldReturnBookingReportRowWithoutLearningProvider() {
        BookingStatus status = BookingStatus.CONFIRMED;
        String learnerUid = "learner-uid";
        String eventUid = "event-uid";
        String name = "learner name";
        String profession = "profession1";
        String otherAreasOfWork = "commercial, digital";
        String organisation = "_department";
        String grade = "_grade";
        String email = "user@example.org";
        String courseId = "course-id";
        String courseTitle = "course-title";
        String moduleId = "module-id";
        String moduleTitle = "module-title";

        Booking booking = new Booking();
        booking.setStatus(status);
        booking.setLearner(learnerUid);
        booking.setLearnerEmail(email);
        booking.setEvent(eventUid);

        CivilServant civilServant = new CivilServant();
        civilServant.setId(learnerUid);
        civilServant.setName(name);
        civilServant.setProfession(profession);
        civilServant.setOtherAreasOfWork(otherAreasOfWork);
        civilServant.setOrganisation(organisation);
        civilServant.setGrade(grade);

        Course course = new Course();
        course.setId(courseId);
        course.setTitle(courseTitle);

        Module module = new Module();
        module.setId(moduleId);
        module.setTitle(moduleTitle);
        module.setRequired(true);
        module.setAssociatedLearning(true);
        module.setCourse(course);

        Event event = new Event();
        event.setId(eventUid);
        event.setModule(module);

        Identity identity = new Identity();
        identity.setUsername(email);
        identity.setUid(learnerUid);

        BookingReportRow reportRow = reportRowFactory.createBookingReportRow(Optional.of(civilServant), Optional.of(event), booking, identity, false);

        assertEquals(learnerUid, reportRow.getLearnerId());
        assertEquals(name, reportRow.getName());
        assertEquals(profession, reportRow.getProfession());
        assertEquals(String.join(", ", otherAreasOfWork), reportRow.getOtherAreasOfWork());
        assertEquals(organisation, reportRow.getDepartment());
        assertEquals(grade, reportRow.getGrade());
        assertEquals(email, reportRow.getEmail());
        assertEquals(courseId, reportRow.getCourseId());
        assertEquals(courseTitle, reportRow.getCourseTitle());
        assertEquals(moduleId, reportRow.getModuleId());
        assertEquals(moduleTitle, reportRow.getModuleTitle());
        assertNull(reportRow.getLearningProvider());
    }

    @Test
    public void shouldReturnModuleReportRow() {

        String moduleState = "COMPLETED";
        String stateChangeDate = "2018-01-01T00:00:00";

        String learnerUid = "learner-uid";
        String name = "learner name";
        String profession = "profession1";
        String otherAreasOfWork = "commercial, digital";
        String organisation = "_department";
        String grade = "_grade";
        String email = "user@example.org";
        String courseId = "course-id";
        String courseTitle = "course-title";
        String moduleId = "module-id";
        String moduleTitle = "module-title";
        String moduleType = "module-type";

        ModuleRecord moduleRecord = new ModuleRecord();
        moduleRecord.setLearner(learnerUid);
        moduleRecord.setModuleId(moduleId);
        moduleRecord.setState(moduleState);
        moduleRecord.setStateChangeDate(stateChangeDate);
        moduleRecord.setModuleType(moduleType);
        moduleRecord.setCourseId(courseId);
        moduleRecord.setCourseTitle(courseTitle);

        CivilServant civilServant = new CivilServant();
        civilServant.setId(learnerUid);
        civilServant.setName(name);
        civilServant.setProfession(profession);
        civilServant.setOtherAreasOfWork(otherAreasOfWork);
        civilServant.setOrganisation(organisation);
        civilServant.setGrade(grade);
        civilServant.setEmail(email);

        Course course = new Course();
        course.setId(courseId);
        course.setTitle(courseTitle);

        Module module = new Module();
        module.setId(moduleId);
        module.setTitle(moduleTitle);
        module.setType(moduleType);
        module.setRequired(true);
        module.setAssociatedLearning(true);
        module.setCourse(course);

        Identity identity = new Identity();
        identity.setUsername(email);
        identity.setUid(learnerUid);

        ModuleReportRow reportRow = reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord, identity, false);

        assertEquals(learnerUid, reportRow.getLearnerId());
        assertEquals(name, reportRow.getName());
        assertEquals(profession, reportRow.getProfession());
        assertEquals(String.join(", ", otherAreasOfWork), reportRow.getOtherAreasOfWork());
        assertEquals(organisation, reportRow.getDepartment());
        assertEquals(grade, reportRow.getGrade());
        assertEquals(email, reportRow.getEmail());
        assertEquals(courseId, reportRow.getCourseId());
        assertEquals(courseTitle, reportRow.getCourseTitle());
        assertEquals(moduleId, reportRow.getModuleId());
        assertEquals(moduleTitle, reportRow.getModuleTitle());
        assertEquals(moduleType, reportRow.getModuleType());

        assertEquals(moduleState, reportRow.getStatus());
        assertEquals(stateChangeDate, reportRow.getUpdatedAt());
    }
}
