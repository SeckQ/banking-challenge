package com.challenge.accountservice.infraestructure.input.adapter.rest.impl;

import com.challenge.accountservice.application.input.port.AccountUseCase;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountRequestDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountResponseDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountStatusDTO;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountMapperRest accountMapperRest;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<AccountResponseDTO>> createAccount(@Valid @RequestBody AccountRequestDTO requestDTO) {
        log.info("Received request to create account: {}", requestDTO);
        return Mono.fromCallable(() -> {
                    Account account = accountMapperRest.toDomain(requestDTO);
                    Account saved = accountUseCase.createAccount(account);
                    String clientName = accountUseCase.getClientNameById(account.getClientId());
                    return accountMapperRest.toResponseDTO(saved, clientName);
                })
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountResponseDTO>> getAccountById(@PathVariable Long id) {
        log.info("Received request to get account by id: {}", id);
        return Mono.fromCallable(() -> {
                    Account account = accountUseCase.getAccountById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
                    String clientName = accountUseCase.getClientNameById(account.getClientId());
                    return accountMapperRest.toResponseDTO(account, clientName);
                })
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Flux<AccountResponseDTO> getAllAccounts() {
        log.info("Received request to list all accounts");
        return Mono.fromCallable(accountUseCase::getAllAccounts)
                .doOnNext(list -> log.info("Returning {} accounts", list.size()))
                .flatMapMany(accounts -> Flux.fromIterable(
                        accounts.stream()
                                .map(account -> accountMapperRest.toResponseDTO(account, accountUseCase.getClientNameById(account.getClientId())))
                                .toList()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountResponseDTO>> updateAccount(@Valid @PathVariable Long id,
                                                                  @RequestBody AccountRequestDTO requestDTO) {
        log.info("Received request to update account with id {}: {}", id, requestDTO);
        return Mono.fromCallable(() -> {
                    Account account = accountMapperRest.toDomain(requestDTO);
                    Account updated = accountUseCase.updateAccount(id, account);
                    String clientName = accountUseCase.getClientNameById(account.getClientId());
                    return accountMapperRest.toResponseDTO(updated, clientName);
                })
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable Long id) {
        log.info("Received request to delete account with id: {}", id);
        return Mono.fromRunnable(() -> accountUseCase.deleteAccount(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<AccountResponseDTO>> toggleAccountStatus(@PathVariable Long id,
                                                                         @Valid @RequestBody AccountStatusDTO statusDTO) {
        log.info("Received request to toggle account status for id {}: {}", id, statusDTO.getStatus());
        return Mono.fromCallable(() -> {
                    Account account = accountUseCase.updateAccountStatus(id, statusDTO.getStatus());
                    String clientName = accountUseCase.getClientNameById(account.getClientId());
                    return accountMapperRest.toResponseDTO(account, clientName);
                })
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
