package com.challenge.accountservice.application.service.impl;

import com.challenge.accountservice.application.output.port.AccountRepositoryPort;
import com.challenge.accountservice.application.service.AccountService;
import com.challenge.accountservice.application.output.port.CustomerClient;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
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
    private final CustomerClient customerClient;

    @Override
    public Account createAccount(Account account) {
        log.info("Creating account for clientId: {}", account.getClientId());

        String clientName = customerClient.getCustomerNameById(account.getClientId());
        if (clientName == null || clientName.equals("UNKNOWN")) {
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

    public String getClientNameById(Long clientId) {
        return customerClient.getCustomerNameById(clientId);
    }
}
