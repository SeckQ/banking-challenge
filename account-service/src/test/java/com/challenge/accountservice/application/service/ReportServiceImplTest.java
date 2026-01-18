package com.challenge.accountservice.application.service;

import com.challenge.accountservice.application.output.port.ReportRepositoryPort;
import com.challenge.accountservice.application.service.impl.ReportServiceImpl;
import com.challenge.accountservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReportServiceImplTest {

    @Mock
    private ReportRepositoryPort reportRepositoryPort;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateReport_shouldReturnFilteredStatements() {

        Long clientId = 1L;
        String clientName = "Juan Pérez";
        LocalDate startDate = LocalDate.of(2025, 8, 1);
        LocalDate endDate = LocalDate.of(2025, 8, 31);

        Account account = new Account(
                1L,
                "478758",
                AccountType.AHORROS,
                2000.0,
                true,
                clientId
        );

        Movement movement1 = new Movement(
                1L,
                LocalDate.of(2025, 8, 5),
                MovementType.DEBITO,
                575.0,
                1425.0,
                account
        );

        Movement movement2 = new Movement(
                2L,
                LocalDate.of(2025, 7, 25), // fuera del rango
                MovementType.CREDITO,
                600.0,
                2025.0,
                account
        );

        when(reportRepositoryPort.findAccountsByClientId(clientId)).thenReturn(List.of(account));
        when(reportRepositoryPort.getClientNameById(clientId)).thenReturn(clientName);
        when(reportRepositoryPort.findMovementsByAccountId(account.getId()))
                .thenReturn(List.of(movement1, movement2));


        List<AccountStatement> report = reportService.generateReport(clientId, startDate, endDate);


        assertEquals(1, report.size());
        AccountStatement statement = report.get(0);

        assertEquals(LocalDate.of(2025, 8, 5), statement.getDate());
        assertEquals("Juan Pérez", statement.getClientName());
        assertEquals("478758", statement.getAccountNumber());
        assertEquals("AHORROS", statement.getAccountType());
        assertEquals(2000.0, statement.getInitialBalance());
        assertEquals(true, statement.getStatus());
        assertEquals("DEBITO", statement.getMovementType());
        assertEquals(575.0, statement.getMovementValue());
        assertEquals(1425.0, statement.getAvailableBalance());


        verify(reportRepositoryPort).findAccountsByClientId(clientId);
        verify(reportRepositoryPort).getClientNameById(clientId);
        verify(reportRepositoryPort).findMovementsByAccountId(account.getId());
    }
}
