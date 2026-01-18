package com.challenge.accountservice.infraestructure.messaging.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CustomerCache {

    private final Map<Long, String> customerNames = new ConcurrentHashMap<>();

    public void put(Long customerId, String name) {
        log.info("Storing customer in cache: customerId={}, name={}", customerId, name);
        customerNames.put(customerId, name);
    }

    public String get(Long customerId) {
        String name = customerNames.get(customerId);
        log.debug("Retrieved customer from cache: customerId={}, name={}", customerId, name);
        return name != null ? name : "UNKNOWN";
    }

    public void remove(Long customerId) {
        log.info("Removing customer from cache: customerId={}", customerId);
        customerNames.remove(customerId);
    }

    public boolean contains(Long customerId) {
        return customerNames.containsKey(customerId);
    }

    public int size() {
        return customerNames.size();
    }
}
