package com.challenge.customerservice.infraestructure.output.adapter;

import com.challenge.customerservice.application.output.port.CustomerRepositoryPort;
import com.challenge.customerservice.domain.model.Customer;
import com.challenge.customerservice.infraestructure.exception.CustomerNotFoundException;
import com.challenge.customerservice.infraestructure.output.repository.JpaCustomerRepository;
import com.challenge.customerservice.infraestructure.output.repository.entity.CustomerEntity;
import com.challenge.customerservice.infraestructure.output.repository.mapper.CustomerMapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final JpaCustomerRepository jpaCustomerRepository;
    private final CustomerMapperRepository customerMapperRepository;


    @Override
    public Customer save(Customer customer) {
        return customerMapperRepository.toDomain(jpaCustomerRepository.save(customerMapperRepository.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return jpaCustomerRepository.findById(id)
                .map(customerMapperRepository::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaCustomerRepository.findAll().stream()
                .map(customerMapperRepository::toDomain)
                .toList();
    }

    @Override
    public Customer update(Long id, Customer customer) {
        CustomerEntity existingCustomer = jpaCustomerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        customer.setId(id);
        return save(customer);
    }

    @Override
    public void deleteById(Long id) {
        if (!jpaCustomerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        jpaCustomerRepository.deleteById(id);
    }
}
