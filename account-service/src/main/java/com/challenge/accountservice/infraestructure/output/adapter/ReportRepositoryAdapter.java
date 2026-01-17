package com.challenge.accountservice.infraestructure.output.adapter;

import com.challenge.accountservice.application.output.port.ReportRepositoryPort;
import com.challenge.accountservice.application.output.port.CustomerClient;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.infraestructure.output.repository.AccountJpaRepository;
import com.challenge.accountservice.infraestructure.output.repository.MovementJpaRepository;
import com.challenge.accountservice.infraestructure.output.repository.mapper.AccountMapperRepository;
import com.challenge.accountservice.infraestructure.output.repository.mapper.MovementMapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepositoryPort {

    private final AccountJpaRepository accountJpaRepository;
    private final MovementJpaRepository movementJpaRepository;
    private final CustomerClient customerClient;
    private final AccountMapperRepository accountMapperRepository;
    private final MovementMapperRepository movementMapperRepository;

    @Override
    public List<Account> findAccountsByClientId(Long clientId) {
        return accountJpaRepository.findAll().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .map(accountMapperRepository::toDomain) // <--- mapeo aquí
                .filter(a -> a.getClientId().equals(clientId)) // ahora sobre el modelo de dominio
                .toList();
    }

    @Override
    public List<Movement> findMovementsByAccountId(Long accountId) {
        return movementJpaRepository.findByAccountId(accountId).stream()
                .map(movementMapperRepository::toDomain)
                .toList();
    }

    @Override
    public String getClientNameById(Long clientId) {
        return customerClient.getCustomerNameById(clientId);
    }
}
