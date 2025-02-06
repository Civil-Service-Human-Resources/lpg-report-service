package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.notification.INotificationClient;
import uk.gov.cshr.report.dto.MessageDto;

@Service
public class NotificationService {
    private final INotificationClient notificationClient;

    public NotificationService(INotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendEmail(MessageDto messageDto) {
        notificationClient.sendEmail(messageDto);
    }

}
