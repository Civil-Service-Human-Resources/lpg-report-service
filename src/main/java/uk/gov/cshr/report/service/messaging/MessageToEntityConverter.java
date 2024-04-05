package uk.gov.cshr.report.service.messaging;

import uk.gov.cshr.report.service.messaging.model.IMessageMetadata;
import uk.gov.cshr.report.service.messaging.model.Message;

public interface MessageToEntityConverter <MessageMetadata extends IMessageMetadata, Entity> {
    Entity convert(Message<MessageMetadata> message);
}
