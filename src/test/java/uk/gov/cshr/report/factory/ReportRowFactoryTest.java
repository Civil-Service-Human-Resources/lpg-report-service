package uk.gov.cshr.report.factory;

import org.junit.Test;
import uk.gov.cshr.report.domain.catalogue.Course;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.LearningProvider;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReportRowFactoryTest {

    private ReportRowFactory reportRowFactory = new ReportRowFactory();

    @Test
    public void shouldReturnBookingReportRowWithLearningProvider() {
        BookingStatus status = BookingStatus.CONFIRMED;
        String learnerUid = "learner-uid";
        String eventUid = "event-uid";
        String name = "learner name";
        String profession = "profession1";
        List<String> otherAreasOfWork = Arrays.asList("profession2", "profession3");
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
        module.setRequired(true);
        module.setCourse(course);

        LearningProvider learningProvider = new LearningProvider();
        learningProvider.setId(learningProviderId);
        learningProvider.setName(learningProviderName);

        Event event = new Event();
        event.setId(eventUid);
        event.setModule(module);
        event.setLearningProvider(learningProvider);

        BookingReportRow reportRow = reportRowFactory.createBookingReportRow(civilServant, event, booking);

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
    public void shouldReturnBookingReportRowWithoutLearningProvider() {
        BookingStatus status = BookingStatus.CONFIRMED;
        String learnerUid = "learner-uid";
        String eventUid = "event-uid";
        String name = "learner name";
        String profession = "profession1";
        List<String> otherAreasOfWork = Arrays.asList("profession2", "profession3");
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
        booking.setEvent(eventUid);

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
        module.setRequired(true);
        module.setCourse(course);

        Event event = new Event();
        event.setId(eventUid);
        event.setModule(module);

        BookingReportRow reportRow = reportRowFactory.createBookingReportRow(civilServant, event, booking);

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
        List<String> otherAreasOfWork = Arrays.asList("profession2", "profession3");
        String organisation = "_department";
        String grade = "_grade";
        String email = "user@example.org";
        String courseId = "course-id";
        String courseTitle = "course-title";
        String moduleId = "module-id";
        String moduleTitle = "module-title";

        ModuleRecord moduleRecord = new ModuleRecord();
        moduleRecord.setLearner(learnerUid);
        moduleRecord.setModuleId(moduleId);
        moduleRecord.setState(moduleState);
        moduleRecord.setStateChangeDate(stateChangeDate);

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
        module.setRequired(true);
        module.setCourse(course);

        ModuleReportRow reportRow = reportRowFactory.createModuleReportRow(civilServant, module, moduleRecord);

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

        assertEquals(moduleState, reportRow.getStatus());
        assertEquals(stateChangeDate, reportRow.getDate());
    }
}