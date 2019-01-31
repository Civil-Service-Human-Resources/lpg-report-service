package uk.gov.cshr.report.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LearnerRecordEvent {
    private String bookingReference;
    private String courseId;
    private String courseName;
    private String moduleId;
    private String moduleName;
    private String eventId;
    private BigDecimal cost;
    private LocalDateTime date;
    private String delegateName;
    private String delegateEmailAddress;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentMethod;
    private String paymentDetails;
}
