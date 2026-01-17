package com.challenge.accountservice.application.input.port;

import com.challenge.accountservice.domain.model.AccountStatement;

import java.time.LocalDate;
import java.util.List;

public interface ReportUseCase {
    List<AccountStatement> generateReport(Long clientId, LocalDate startDate, LocalDate endDate);
}
