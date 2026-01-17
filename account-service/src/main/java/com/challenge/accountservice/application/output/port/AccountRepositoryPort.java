package com.challenge.accountservice.application.output.port;

import com.challenge.accountservice.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findById(Long id);
    List<Account> findAll();
    Account update(Long id, Account account);
    void deleteById(Long id);
    boolean existsById(Long id);

    Optional<Account> findByAccountNumber(String accountNumber);
}
