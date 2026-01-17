package com.challenge.customerservice.infraestructure.input.adapter.rest.impl;

import com.challenge.customerservice.application.input.port.CustomerUseCase;
import com.challenge.customerservice.domain.model.Customer;
import com.challenge.customerservice.infraestructure.input.adapter.rest.bean.CustomerRequest;
import com.challenge.customerservice.infraestructure.input.adapter.rest.bean.CustomerResponse;
import com.challenge.customerservice.infraestructure.input.adapter.rest.mapper.CustomerMapperRest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerUseCase customerUseCase;
    private final CustomerMapperRest mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Request received to create customer: {}", customerRequest);
        return Mono.fromCallable(() -> {
            Customer customer = mapper.toDomain(customerRequest);
            Customer saved = customerUseCase.createCustomer(customer);
            return ResponseEntity.ok(mapper.toResponse(saved));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(@PathVariable Long id) {
        log.info("Request received to get customer with id: {}", id);
        return Mono.fromCallable(() ->
                customerUseCase.getCustomerById(id)
                        .map(mapper::toResponse)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> {
                            log.error("Customer with ID {} not found", id);
                            return ResponseEntity.notFound().build();
                        })
        ).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Flux<CustomerResponse> getAllCustomers() {
        log.info("Request received to get all customers");
        return Mono.fromCallable(customerUseCase::getAllCustomers)
                .doOnNext(list -> log.info("Found {} customers", list.size()))
                .flatMapMany(customers ->
                        Flux.fromStream(customers.stream().map(mapper::toResponse))
                )
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(@PathVariable Long id,
                                                                 @RequestBody CustomerRequest customerRequest) {
        log.info("Request received to update customer with id: {}", id);
        return Mono.fromCallable(() -> {
            Customer customer = mapper.toDomain(customerRequest);
            Customer updated = customerUseCase.updateCustomer(id, customer);
            log.info("Customer with ID {} updated successfully", id);
            return ResponseEntity.ok(mapper.toResponse(updated));
        }).doOnError(e -> log.error("Error updating customer with ID {}: {}", id, e.getMessage()))
        .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Long id) {
        log.info("Request received to delete customer with id: {}", id);
        return Mono.fromRunnable(() -> {
                    customerUseCase.deleteCustomer(id);
                    log.info("Customer with ID {} deleted successfully", id);
                }).doOnError(e -> log.error("Error deleting customer with ID {}: {}", id, e.getMessage()))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
