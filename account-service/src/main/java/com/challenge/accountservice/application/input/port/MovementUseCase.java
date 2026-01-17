package com.challenge.accountservice.application.input.port;

import com.challenge.accountservice.domain.model.Movement;

import java.util.List;
import java.util.Optional;

public interface MovementUseCase {
    Movement createMovement(Movement movement);
    Optional<Movement> getMovementById(Long id);
    List<Movement> getAllMovements();
    void deleteMovement(Long id);
}
