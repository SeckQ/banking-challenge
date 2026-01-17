package com.challenge.customerservice.infraestructure.output.repository.mapper;

import com.challenge.customerservice.domain.model.Customer;
import com.challenge.customerservice.infraestructure.output.repository.entity.CustomerEntity;
import com.challenge.customerservice.infraestructure.output.repository.entity.PersonEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapperRepository {

    @Mapping(source = "person.id", target = "id")
    @Mapping(source = "person.identification", target = "identification")
    @Mapping(source = "person.name", target = "name")
    @Mapping(source = "person.gender", target = "gender")
    @Mapping(source = "person.address", target = "address")
    @Mapping(source = "person.phone", target = "phone")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "status", target = "status")
    Customer toDomain(CustomerEntity customerEntity);

    @InheritInverseConfiguration
    @Mapping(target = "person", ignore = true)
    CustomerEntity toEntity(Customer customer);

    default PersonEntity mapToPersonEntity(Customer customer) {
        PersonEntity person = new PersonEntity();
        person.setId(customer.getId());
        person.setIdentification(customer.getIdentification());
        person.setName(customer.getName());
        person.setGender(customer.getGender());
        person.setAddress(customer.getAddress());
        person.setPhone(customer.getPhone());
        return person;
    }

    @AfterMapping
    default void fillPersonEntity(Customer customer, @MappingTarget CustomerEntity entity) {
        entity.setPerson(mapToPersonEntity(customer));
    }
}
