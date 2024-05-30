package uk.gov.cshr.report.service.messaging;

public interface IQueueClient {
    void receiveMessage(String message);
}
