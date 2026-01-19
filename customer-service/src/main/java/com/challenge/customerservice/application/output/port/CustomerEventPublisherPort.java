package com.challenge.customerservice.application.output.port;

import com.challenge.customerservice.infraestructure.messaging.event.CustomerEvent;

public interface CustomerEventPublisherPort {
    void publishCustomerEvent(CustomerEvent event);
}
