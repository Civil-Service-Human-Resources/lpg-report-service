package uk.gov.cshr.report.factory;

import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.reports.BookingReportRow;

public class ReportRowFactory {
    public BookingReportRow createBookingReportRow(CivilServant civilServant, Event event, Booking booking) {
        BookingReportRow reportRow = new BookingReportRow();
        reportRow.setLearnerid(civilServant.getUid());
        reportRow.setName(civilServant.getName());
        reportRow.setEmail(civilServant.getEmail());
        reportRow.setDepartment(civilServant.getDepartment());
        reportRow.setProfession(civilServant.getProfession());
        reportRow.setOtherAreasOfWork(String.join(", ", civilServant.getOtherAreasOfWork()));
        reportRow.setGrade(civilServant.getGrade());
        reportRow.setCourseId(event.getCourse().getId());
        reportRow.setCourseTitle(event.getCourse().getTitle());
        reportRow.setModuleId(event.getModule().getId());
        reportRow.setModuleTitle(event.getModule().getTitle());
        reportRow.setStatus(booking.getStatus().getValue());
        reportRow.setLearningProvider(event.getLearningProvider().getName());
        reportRow.setRequired(event.getModule().getRequired());

        return reportRow;
    }
}
