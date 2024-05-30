package uk.gov.cshr.report.config.messaging;

import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Slf4j
@Configuration
public class JmsConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(value = "spring.jms.servicebus.enabled", havingValue = "false")
    public ConnectionFactory getActiveMQConnectionFactory(ActiveMQJmsConfigProperties properties) {
        log.info("Service bus is disabled; defaulting to activeMQ/Artemis");
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
