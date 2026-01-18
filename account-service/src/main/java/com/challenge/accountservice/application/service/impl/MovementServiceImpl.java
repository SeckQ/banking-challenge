package com.challenge.accountservice.application.service.impl;

import com.challenge.accountservice.application.output.port.AccountRepositoryPort;
import com.challenge.accountservice.application.output.port.MovementRepositoryPort;
import com.challenge.accountservice.application.service.MovementService;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.infraestructure.exception.InsufficientFundsException;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepositoryPort movementRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public Movement createMovement(Movement movement) {
        log.info("Creating movement for accountId: {}", movement.getAccount().getAccountNumber());
        try {
            if (movement.getValue() == null || movement.getValue() == 0) {
                log.error("Invalid movement value: {}", movement.getValue());
                throw new IllegalArgumentException("Movement value cannot be zero or null");
            }

            Account account = accountRepositoryPort.findByAccountNumber(movement.getAccount().getAccountNumber())
                    .orElseThrow(() -> {
                        log.error("Account not found for number: {}", movement.getAccount().getAccountNumber());
                        return new ResourceNotFoundException("Account not found with number: " + movement.getAccount().getAccountNumber());
                    });

            double lastBalance = movementRepositoryPort
                    .getLastBalanceByAccountNumber(account.getAccountNumber())
                    .orElse(account.getInitialBalance());

            double newBalance = lastBalance + movement.getValue();

            if (newBalance < 0) {
                log.error("Insufficient funds. Current balance: {}, Movement value: {}, Resulting balance: {}", 
                    lastBalance, movement.getValue(), newBalance);
                throw new InsufficientFundsException("Saldo no disponible");
            }

            movement.setDate(LocalDate.now());
            movement.setBalance(newBalance);
            movement.setAccount(account);

            Movement saved = movementRepositoryPort.save(movement);

            log.info("Movement created successfully for accountNumber: {}. Value: {}, New balance: {}", 
                movement.getAccount().getAccountNumber(), movement.getValue(), newBalance);
            return saved;
        } catch (Exception ex) {
            log.error("Error while creating movement for accountId: {}", movement.getAccount().getAccountNumber(), ex);
            throw ex;
        }
    }

    @Override
    public List<Movement> getAllMovements() {
        log.info("Retrieving all movements");
        return movementRepositoryPort.findAll();
    }


    @Override
    public Optional<Movement> getMovementById(Long id) {
        log.info("Retrieving movement by id: {}", id);
        return movementRepositoryPort.findById(id);
    }

    @Override
    public void deleteMovement(Long id) {
        log.info("Deleting movement with id: {}", id);
        if (!movementRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("Movement not found with id: " + id);
        }
        movementRepositoryPort.deleteById(id);
    }
}