package com.challenge.accountservice.infraestructure.output.adapter;

import com.challenge.accountservice.application.output.port.AccountRepositoryPort;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
import com.challenge.accountservice.infraestructure.output.repository.AccountJpaRepository;
import com.challenge.accountservice.infraestructure.output.repository.entity.AccountEntity;
import com.challenge.accountservice.infraestructure.output.repository.mapper.AccountMapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {
    private final AccountJpaRepository accountJpaRepository;
    private final AccountMapperRepository accountMapper;


    @Override
    public Account save(Account account) {
        return accountMapper.toDomain(
                accountJpaRepository.save(accountMapper.toEntity(account))
        );
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountJpaRepository.findById(id).map(accountMapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return accountJpaRepository.findAll()
                .stream()
                .map(accountMapper::toDomain)
                .toList();
    }

    @Override
    public Account update(Long id, Account account) {
        AccountEntity existingAccount = accountJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        account.setId(id);
        return save(account);
    }

    @Override
    public void deleteById(Long id) {
        accountJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return accountJpaRepository.existsById(id);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountJpaRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toDomain);
    }
}
