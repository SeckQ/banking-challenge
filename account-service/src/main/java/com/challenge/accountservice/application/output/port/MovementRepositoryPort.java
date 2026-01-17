package com.challenge.accountservice.application.output.port;

import com.challenge.accountservice.domain.model.Movement;

import java.util.List;
import java.util.Optional;

public interface MovementRepositoryPort {
    Movement save (Movement movement);
    Optional<Movement> findById(Long id);
    List<Movement> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Double> getLastBalanceByAccountNumber(String accountNumber);
}
