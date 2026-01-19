package com.challenge.customerservice.infraestructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEvent implements Serializable {
    private Long customerId;
    private String name;
    private String eventType; // CREATED, UPDATED, DELETED
}
