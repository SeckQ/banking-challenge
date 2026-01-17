package com.challenge.accountservice.application.service.impl;

import com.challenge.accountservice.application.output.port.ReportRepositoryPort;
import com.challenge.accountservice.application.service.ReportService;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.AccountStatement;
import com.challenge.accountservice.domain.model.Movement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepositoryPort reportRepositoryPort;

    @Override
    public List<AccountStatement> generateReport(Long clientId, LocalDate startDate, LocalDate endDate) {
        log.info("Generating report for clientId {} from {} to {}", clientId, startDate, endDate);

        List<Account> accounts = reportRepositoryPort.findAccountsByClientId(clientId);
        String clientName = reportRepositoryPort.getClientNameById(clientId);

        List<AccountStatement> report = new ArrayList<>();
        for (Account account : accounts) {
            List<Movement> movements = reportRepositoryPort.findMovementsByAccountId(account.getId())
                    .stream()
                    .filter(m -> !m.getDate().isBefore(startDate) && !m.getDate().isAfter(endDate))
                    .toList();

            for (Movement movement : movements) {
                report.add(new AccountStatement(
                        movement.getDate(),
                        clientName,
                        account.getAccountNumber(),
                        account.getAccountType().toString(),
                        account.getInitialBalance(),
                        account.getStatus(),
                        movement.getMovementType().toString(),
                        movement.getValue(),
                        movement.getBalance()
                ));
            }
        }

        return report;
    }
}