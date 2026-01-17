package com.challenge.customerservice.infraestructure.output.repository;

import com.challenge.customerservice.infraestructure.output.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCustomerRepository extends JpaRepository<CustomerEntity,Long> {
}
