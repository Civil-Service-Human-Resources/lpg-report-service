package uk.gov.cshr.report.factory;

import org.springframework.stereotype.Component;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.util.Optional;

@Component
public class ReportRowFactory {
    public BookingReportRow createBookingReportRow(Optional<CivilServant> civilServantOptional, Optional<Event> eventOptional, Booking booking, Identity identity, boolean isProfessionReporter) {
        BookingReportRow reportRow = new BookingReportRow();

        if (civilServantOptional.isPresent()) {
            CivilServant civilServant = civilServantOptional.get();

            if (!isProfessionReporter) {
                reportRow.setLearnerId(identity.getUid());
                reportRow.setName(civilServant.getName());
                reportRow.setEmail(identity.getUsername());
            }

            reportRow.setDepartment(civilServant.getOrganisation());
            reportRow.setProfession(civilServant.getProfession());
            reportRow.setGrade(civilServant.getGrade());

            reportRow.setOtherAreasOfWork(civilServant.getOtherAreasOfWork());
        }

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            reportRow.setCourseId(event.getModule().getCourse().getId());
            reportRow.setCourseTitle(event.getModule().getCourse().getTitle());
            reportRow.setTopicId(event.getModule().getCourse().getTopicId());
            reportRow.setModuleId(event.getModule().getId());
            reportRow.setModuleTitle(event.getModule().getTitle());
            reportRow.setRequired(event.getModule().getRequired());
            reportRow.setEventID(event.getId());
            reportRow.setLocation(event.getLocation());
            reportRow.setEventDate(event.getEventDate());
            if (event.getModule().getAssociatedLearning() != null) {
                reportRow.setPaidFor(event.getModule().getAssociatedLearning());
            }
            Optional.ofNullable(event.getLearningProvider()).ifPresent(
                    learningProvider -> reportRow.setLearningProvider(learningProvider.getName())
            );
        }
        reportRow.setBookingReference(booking.getBookingReference());
        reportRow.setStatus(booking.getStatus().getValue());
        reportRow.setBookingTime(booking.getBookingTime());
        reportRow.setConfirmationTime(booking.getConfirmationTime());
        reportRow.setCancellationTime(booking.getCancellationTime());
        reportRow.setAccessibilityOptions(booking.getAccessibilityOptions());
        reportRow.setBookingCancellationReason(booking.getCancellationReason());
        reportRow.setPoNumber(booking.getPoNumber());

        return reportRow;
    }

    public ModuleReportRow createModuleReportRow(CivilServant civilServant, Module module, ModuleRecord moduleRecord, Identity identity, boolean isProfessionReporter) {
        ModuleReportRow reportRow = new ModuleReportRow();

        if (!isProfessionReporter) {
            reportRow.setEmail(identity.getUsername());
            reportRow.setLearnerId(identity.getUid());
            reportRow.setName(civilServant.getName());
        }

        reportRow.setDepartment(civilServant.getOrganisation());
        reportRow.setProfession(civilServant.getProfession());
        reportRow.setOtherAreasOfWork(civilServant.getOtherAreasOfWork());
        reportRow.setGrade(civilServant.getGrade());

        reportRow.setCourseId(module.getCourse().getId());
        reportRow.setCourseTitle(module.getCourse().getTitle());
        reportRow.setCourseTopicId(module.getCourse().getTopicId());
        reportRow.setModuleId(module.getId());
        reportRow.setModuleTitle(module.getTitle());
        reportRow.setModuleType(module.getType());
        if (module.getAssociatedLearning() != null) {
            reportRow.setPaidFor(module.getAssociatedLearning());
        }

        if (moduleRecord.getState() != null) {
            reportRow.setStatus(moduleRecord.getState());
        }
        reportRow.setUpdatedAt(moduleRecord.getStateChangeDate());
        reportRow.setCompletedAt(moduleRecord.getCompletedAt());

        return reportRow;
    }
}
