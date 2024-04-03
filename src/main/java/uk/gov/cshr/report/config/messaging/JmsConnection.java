package uk.gov.cshr.report.config.messaging;

import jakarta.jms.ConnectionFactory;

public interface JmsConnection {

    ConnectionFactory buildConnectionFactory();
    
}
