package com.challenge.accountservice.application.service.impl;

import com.challenge.accountservice.application.output.port.AccountRepositoryPort;
import com.challenge.accountservice.application.service.AccountService;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
import com.challenge.accountservice.infraestructure.messaging.cache.CustomerCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepositoryPort accountRepositoryPort;
    private final CustomerCache customerCache;

    @Override
    public Account createAccount(Account account) {
        log.info("Creating account for clientId: {}", account.getClientId());

        String clientName = customerCache.get(account.getClientId());
        if (clientName.equals("UNKNOWN")) {
            throw new ResourceNotFoundException("Client not found with id: " + account.getClientId());
        }

        return accountRepositoryPort.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        log.info("Retrieving all accounts");
        return accountRepositoryPort.findAll();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        log.info("Retrieving account by id: {}", id);
        return accountRepositoryPort.findById(id);
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        log.info("Updating account with id: {}", id);
        if (!accountRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }

        account.setId(id);
        return accountRepositoryPort.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        log.info("Deleting account with id: {}", id);
        if (!accountRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepositoryPort.deleteById(id);
    }

    @Override
    public Account updateAccountStatus(Long id, Boolean status) {
        log.info("Updating account status for id: {} to {}", id, status);
        Account account = accountRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        
        account.setStatus(status);
        return accountRepositoryPort.save(account);
    }

    public String getClientNameById(Long clientId) {
        return customerCache.get(clientId);
    }
}
