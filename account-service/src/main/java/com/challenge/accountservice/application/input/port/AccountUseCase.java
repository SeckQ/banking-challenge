package com.challenge.accountservice.application.input.port;

import com.challenge.accountservice.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountUseCase {
    Account createAccount(Account account);
    Optional<Account> getAccountById(Long id);
    List<Account> getAllAccounts();
    Account updateAccount(Long id, Account account);
    void deleteAccount(Long id);
    String getClientNameById(Long clientId);
    Account updateAccountStatus(Long id, Boolean status);
}
