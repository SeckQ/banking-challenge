package com.challenge.accountservice.infraestructure.output.adapter;

import com.challenge.accountservice.application.output.port.MovementRepositoryPort;
import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.infraestructure.output.repository.MovementJpaRepository;
import com.challenge.accountservice.infraestructure.output.repository.mapper.MovementMapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepositoryPort {

    private final MovementJpaRepository movementJpaRepository;
    private final MovementMapperRepository movementMapperRepository;


    @Override
    public Movement save(Movement movement) {
        return movementMapperRepository.toDomain(movementJpaRepository.save(movementMapperRepository.toEntity(movement)));
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return movementJpaRepository.findById(id).map(movementMapperRepository::toDomain);
    }

    @Override
    public List<Movement> findAll() {
        return movementJpaRepository.findAll().stream()
                .map(movementMapperRepository::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        movementJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return  movementJpaRepository.existsById(id);
    }

    @Override
    public Optional<Double> getLastBalanceByAccountNumber(String accountNumber) {
        return movementJpaRepository.findLastBalanceByAccountNumber(accountNumber);
    }
}
