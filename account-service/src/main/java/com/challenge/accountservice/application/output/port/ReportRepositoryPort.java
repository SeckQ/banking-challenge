package com.challenge.accountservice.application.output.port;

import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.Movement;

import java.util.List;

public interface ReportRepositoryPort {
    List<Account> findAccountsByClientId(Long clientId);
    List<Movement> findMovementsByAccountId(Long accountId);
    String getClientNameById(Long clientId);
}

