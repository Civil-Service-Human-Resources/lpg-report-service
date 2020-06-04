package uk.gov.cshr.report.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import uk.gov.cshr.report.domain.identity.Identity;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;

public class QueryResultExtractor {
    private QueryResultExtractor() {
    }

    public static ModuleRecord extractModuleRecord(ResultSet rs) throws SQLException {
        ModuleRecord moduleRecord = new ModuleRecord();
        moduleRecord.setModuleId(rs.getString(1));
        moduleRecord.setState(rs.getString(2));
        moduleRecord.setLearner(rs.getString(3));
        moduleRecord.setStateChangeDate(rs.getString(4));
        moduleRecord.setCompletedAt(rs.getString(5));

        return moduleRecord;
    }

    public static Booking extractBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt(1));
        booking.setAccessibilityOptions(rs.getString(2));
        booking.setBookingTime(rs.getString(3));
        booking.setCancellationReason(rs.getString(4));
        booking.setCancellationTime(rs.getString(5));
        booking.setConfirmationTime(rs.getString(6));
        booking.setEvent(rs.getString(7));
        booking.setLearner(rs.getString(8));
        booking.setPoNumber(rs.getString(9));
        booking.setStatus(BookingStatus.forValue(rs.getString(10)));
        booking.setBookingReference(rs.getString(11));

        return booking;
    }

    public static Identity extractIdentity(ResultSet rs) throws SQLException {
        Identity identity = new Identity();
        identity.setUsername(rs.getString(1));
        identity.setUid(rs.getString(2));

        return identity;
    }

    public static CivilServant extractCivilServant(ResultSet rs) throws SQLException {
        CivilServant civilServant = new CivilServant();
        civilServant.setId(rs.getString(1));
        civilServant.setName(rs.getString(2));
        civilServant.setOrganisation(rs.getString(3));
        civilServant.setProfession(rs.getString(4));
        civilServant.setUuid(rs.getString(5));
        civilServant.setGrade(rs.getString(6));
        civilServant.setOtherAreasOfWork(rs.getString(7));

        return civilServant;
    }
}
