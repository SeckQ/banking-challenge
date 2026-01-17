package com.challenge.customerservice.infraestructure.input.adapter.rest.mapper;

import com.challenge.customerservice.domain.model.Customer;

import com.challenge.customerservice.infraestructure.input.adapter.rest.bean.CustomerRequest;
import com.challenge.customerservice.infraestructure.input.adapter.rest.bean.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapperRest {

    @Mapping(target = "id", ignore = true)
    Customer toDomain(CustomerRequest request);

    CustomerResponse toResponse(Customer customer);
}
