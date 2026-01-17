package com.challenge.accountservice.infraestructure.input.adapter.rest.impl;

import com.challenge.accountservice.application.input.port.MovementUseCase;
import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.MovementDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.mapper.AccountMapperRest;
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
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
@Slf4j
public class MovementController {

    private final MovementUseCase movementUseCase;
    private final AccountMapperRest mapperRest;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<MovementDTO>> createMovement(@Valid @RequestBody MovementDTO requestDTO) {
        log.info("Received request to create movement: {}", requestDTO);
        return Mono.fromCallable(() -> {
                    Movement domain = mapperRest.toDomain(requestDTO);
                    Movement created = movementUseCase.createMovement(domain);
                    return mapperRest.toDto(created);
                })
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovementDTO>> getMovementById(@PathVariable Long id) {
        log.info("Received request to get movement by id: {}", id);
        return Mono.fromCallable(() -> {
                    Movement movement = movementUseCase.getMovementById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Movement not found with id: " + id));;
                    return mapperRest.toDto(movement);
                })
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Flux<MovementDTO> getAllMovements() {
        log.info("Received request to list all movements");
        return Mono.fromCallable(movementUseCase::getAllMovements)
                .doOnNext(list -> log.info("Returning {} movements", list.size()))
                .flatMapMany(list -> Flux.fromStream(list.stream().map(mapperRest::toDto)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovement(@PathVariable Long id) {
        log.info("Received request to delete movement with id: {}", id);
        return Mono.fromRunnable(() -> movementUseCase.deleteMovement(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}