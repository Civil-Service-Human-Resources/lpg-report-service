package uk.gov.cshr.report.service.messaging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ObjectMapperQueueClient implements IQueueClient {

    protected final MessageConverter converter;

    protected ObjectMapperQueueClient(MessageConverter converter) {
        this.converter = converter;
    }
}
