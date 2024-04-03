package uk.gov.cshr.report.config.messaging;

import com.azure.spring.cloud.core.implementation.connectionstring.ServiceBusConnectionString;
import com.azure.spring.jms.ServiceBusJmsConnectionFactory;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.connection.CachingConnectionFactory;

@ConfigurationProperties(prefix = "app.messaging.servicebus")
@RequiredArgsConstructor
public class ServiceBusJmsConfigProperties implements JmsConnection {

    private final String connectionString;
    private final Integer idleTimeout;

    @Override
    public ConnectionFactory buildConnectionFactory() {
        var connectionFactory = new ServiceBusJmsConnectionFactory();
        var serviceBusConnectionString = new ServiceBusConnectionString(connectionString);
        var remoteUri = String.format("amqps://%s?amqp.idleTimeout=%d", serviceBusConnectionString.getEndpointUri(), idleTimeout);
        connectionFactory.setRemoteURI(remoteUri);
        connectionFactory.setUsername(serviceBusConnectionString.getSharedAccessKeyName());
        connectionFactory.setPassword(serviceBusConnectionString.getSharedAccessKey());
        connectionFactory.setExceptionListener(new CustomServiceBusExceptionListener());
        return new CachingConnectionFactory(connectionFactory);
    }

}
