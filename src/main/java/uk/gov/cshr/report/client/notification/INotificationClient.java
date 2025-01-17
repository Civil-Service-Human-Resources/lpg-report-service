package uk.gov.cshr.report.client.notification;

import uk.gov.cshr.report.dto.MessageDto;

public interface INotificationClient {
    void sendEmail(MessageDto messageDto);
}
