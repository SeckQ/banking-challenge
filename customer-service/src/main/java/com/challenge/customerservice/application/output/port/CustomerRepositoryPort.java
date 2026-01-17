package com.challenge.customerservice.application.output.port;

import com.challenge.customerservice.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    Customer update(Long id, Customer customer);
    void deleteById(Long id);
}
