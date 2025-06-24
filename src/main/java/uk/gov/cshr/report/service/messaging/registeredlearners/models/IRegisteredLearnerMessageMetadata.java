package uk.gov.cshr.report.service.messaging.registeredlearners.models;

import uk.gov.cshr.report.service.messaging.model.IMessageMetadata;

public interface IRegisteredLearnerMessageMetadata extends IMessageMetadata {
    RegisteredLearnerOperation getOperation();
    RegisteredLearnerDataType getDataType();
    <T> T getData();
}
