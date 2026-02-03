package uk.gov.cshr.report.domain.learnerrecord;

import lombok.Data;

@Data
public class Booking {
    private Integer id;
    private String learner;
    private String eventUid;
    private BookingStatus status;
    private String bookingTime;
    private String confirmationTime;
    private String cancellationTime;
    private String accessibilityOptions;
    private String cancellationReason;
    private String poNumber;
    private String bookingReference;
}
