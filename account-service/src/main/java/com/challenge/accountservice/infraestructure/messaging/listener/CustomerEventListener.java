package com.challenge.accountservice.infraestructure.messaging.listener;

import com.challenge.accountservice.infraestructure.messaging.cache.CustomerCache;
import com.challenge.accountservice.infraestructure.messaging.config.RabbitMQConfig;
import com.challenge.accountservice.infraestructure.messaging.event.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {

    private final CustomerCache customerCache;

    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_QUEUE)
    public void handleCustomerEvent(CustomerEvent event) {
        log.info("Received customer event: {} for customerId: {}", event.getEventType(), event.getCustomerId());

        switch (event.getEventType()) {
            case "CREATED":
            case "UPDATED":
                customerCache.put(event.getCustomerId(), event.getName());
                log.info("Customer cached/updated: customerId={}, name={}", event.getCustomerId(), event.getName());
                break;
            case "DELETED":
                customerCache.remove(event.getCustomerId());
                log.info("Customer removed from cache: customerId={}", event.getCustomerId());
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}
