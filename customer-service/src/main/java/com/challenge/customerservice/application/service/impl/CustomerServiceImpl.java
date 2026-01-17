package com.challenge.customerservice.application.service.impl;

import com.challenge.customerservice.application.input.port.CustomerUseCase;
import com.challenge.customerservice.application.output.port.CustomerRepositoryPort;
import com.challenge.customerservice.domain.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    @Override
    public Customer createCustomer(Customer customer) {
        log.info("Creating customer with identification: {}", customer.getIdentification());

        boolean exists = customerRepositoryPort.findAll().stream()
                .anyMatch(c -> c.getIdentification().equals(customer.getIdentification()));

        if (exists) {
            log.error("Attempt to save duplicated customer with identification: {}", customer.getIdentification());
            throw new IllegalArgumentException("Customer already exists with identification: " + customer.getIdentification());
        }

        Customer saved = customerRepositoryPort.save(customer);
        log.info("Customer saved successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        log.info("Retrieving customer with id: {}", id);
        return customerRepositoryPort.findById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.info("Retrieving all customers");
        List<Customer> customers = customerRepositoryPort.findAll();
        log.info("Found {} customers", customers.size());
        return customers;
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        log.info("Updating customer with id: {}", id);

        if (customerRepositoryPort.findById(id).isEmpty()) {
            log.error("Customer with ID {} not found for update", id);
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }

        customer.setId(id);
        Customer updated = customerRepositoryPort.save(customer);
        log.info("Customer with ID {} updated successfully", id);
        return updated;
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);

        if (customerRepositoryPort.findById(id).isEmpty()) {
            log.error("Customer with ID {} not found for deletion", id);
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }

        customerRepositoryPort.deleteById(id);
        log.info("Customer with ID {} deleted successfully", id);
    }
}