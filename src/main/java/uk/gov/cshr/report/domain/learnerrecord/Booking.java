package uk.gov.cshr.report.domain.learnerrecord;

import lombok.Data;

@Data
public class Booking {
    private String learnerUid;
    private String eventUid;
    private BookingStatus status;
}
