package uk.gov.cshr.report.config.messaging;

import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Slf4j
@Configuration
public class JmsConfig {


    @Bean
    @ConditionalOnProperty(value = "app.messaging.config.preferred-connection", havingValue = "ACTIVEMQ")
    public ConnectionFactory getActiveMQConnectionFactory(ActiveMQJmsConfigProperties properties) {
        return properties.buildConnectionFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "app.messaging.config.preferred-connection", havingValue = "SERVICEBUS")
    public ConnectionFactory getServiceBusConnectionFactory(ServiceBusJmsConfigProperties properties) {
        return properties.buildConnectionFactory();
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory
                = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(new CustomJmsErrorHandler());
        return factory;
    }
}
