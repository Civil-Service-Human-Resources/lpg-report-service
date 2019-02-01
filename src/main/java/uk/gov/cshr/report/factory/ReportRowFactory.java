package uk.gov.cshr.report.factory;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.dto.catalogue.Event;
import uk.gov.cshr.report.dto.catalogue.Module;
import uk.gov.cshr.report.dto.learnerrecord.Booking;
import uk.gov.cshr.report.dto.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.dto.registry.CivilServant;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.util.Optional;

@Component
public class ReportRowFactory {
    public BookingReportRow createBookingReportRow(Optional<CivilServant> civilServantOptional, Optional<Event> eventOptional, Booking booking) {
        BookingReportRow reportRow = new BookingReportRow();

        if (civilServantOptional.isPresent()) {
            CivilServant civilServant = civilServantOptional.get();
            reportRow.setLearnerId(civilServant.getId());
            reportRow.setName(civilServant.getName());
            reportRow.setEmail(civilServant.getEmail());
            reportRow.setDepartment(civilServant.getOrganisation());
            reportRow.setProfession(civilServant.getProfession());
            reportRow.setOtherAreasOfWork(String.join(", ", civilServant.getOtherAreasOfWork()));
            reportRow.setGrade(civilServant.getGrade());
        }

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            reportRow.setCourseId(event.getModule().getCourse().getId());
            reportRow.setCourseTitle(event.getModule().getCourse().getTitle());
            reportRow.setModuleId(event.getModule().getId());
            reportRow.setModuleTitle(event.getModule().getTitle());
            reportRow.setRequired(event.getModule().getRequired());
            Optional.ofNullable(event.getLearningProvider()).ifPresent(
                learningProvider -> reportRow.setLearningProvider(learningProvider.getName())
            );
        }

        reportRow.setStatus(booking.getStatus().getValue());

        return reportRow;
    }

    public ModuleReportRow createModuleReportRow(CivilServant civilServant, Module module, ModuleRecord moduleRecord) {
        ModuleReportRow reportRow = new ModuleReportRow();
        reportRow.setLearnerId(civilServant.getId());
        reportRow.setName(civilServant.getName());
        reportRow.setEmail(civilServant.getEmail());
        reportRow.setDepartment(civilServant.getOrganisation());
        reportRow.setProfession(civilServant.getProfession());
        reportRow.setOtherAreasOfWork(String.join(", ", civilServant.getOtherAreasOfWork()));
        reportRow.setGrade(civilServant.getGrade());

        reportRow.setCourseId(module.getCourse().getId());
        reportRow.setCourseTitle(module.getCourse().getTitle());
        reportRow.setModuleId(module.getId());
        reportRow.setModuleTitle(module.getTitle());
        reportRow.setModuleType(module.getType());

        reportRow.setStatus(moduleRecord.getState());
        reportRow.setDate(moduleRecord.getStateChangeDate());

        return reportRow;
    }
}
