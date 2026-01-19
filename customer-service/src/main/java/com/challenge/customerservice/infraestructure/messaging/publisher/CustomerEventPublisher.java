package com.challenge.customerservice.infraestructure.messaging.publisher;

import com.challenge.customerservice.application.output.port.CustomerEventPublisherPort;
import com.challenge.customerservice.infraestructure.messaging.config.RabbitMQConfig;
import com.challenge.customerservice.infraestructure.messaging.event.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventPublisher implements CustomerEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public void publishCustomerEvent(CustomerEvent event) {
        log.info("Publishing customer event: {} for customerId: {}", event.getEventType(), event.getCustomerId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                RabbitMQConfig.CUSTOMER_ROUTING_KEY,
                event
        );
        log.info("Customer event published successfully");
    }
}
