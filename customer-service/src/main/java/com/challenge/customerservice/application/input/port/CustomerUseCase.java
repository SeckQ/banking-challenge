package com.challenge.customerservice.application.input.port;

import com.challenge.customerservice.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerUseCase {
    Customer createCustomer(Customer customer);
    Optional<Customer> getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}
