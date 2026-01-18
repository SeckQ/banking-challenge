package com.challenge.accountservice.application.service;

import com.challenge.accountservice.application.output.port.AccountRepositoryPort;
import com.challenge.accountservice.application.output.port.MovementRepositoryPort;
import com.challenge.accountservice.application.service.impl.MovementServiceImpl;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.domain.model.MovementType;
import com.challenge.accountservice.infraestructure.exception.InsufficientFundsException;
import com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovementServiceImplTest {

    private MovementRepositoryPort movementRepository;
    private AccountRepositoryPort accountRepository;
    private MovementServiceImpl movementService;

    @BeforeEach
    void setUp() {
        movementRepository = mock(MovementRepositoryPort.class);
        accountRepository = mock(AccountRepositoryPort.class);
        movementService = new MovementServiceImpl(movementRepository, accountRepository);
    }

    @Test
    void createMovement_credit_shouldIncreaseBalance() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 500.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.CREDITO, 200.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));
        when(movementRepository.getLastBalanceByAccountNumber("123")).thenReturn(Optional.of(500.0));
        when(movementRepository.save(any(Movement.class))).thenAnswer(inv -> {
            Movement m = inv.getArgument(0);
            m.setId(1L);
            m.setDate(LocalDate.now());
            return m;
        });

        Movement result = movementService.createMovement(mov);

        assertNotNull(result.getId());
        assertEquals(700.0, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createMovement_debit_shouldDecreaseBalance() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 500.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.DEBITO, -100.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));
        when(movementRepository.getLastBalanceByAccountNumber("123")).thenReturn(Optional.of(500.0));
        when(movementRepository.save(any(Movement.class))).thenAnswer(inv -> {
            Movement m = inv.getArgument(0);
            m.setId(1L);
            m.setDate(LocalDate.now());
            return m;
        });

        Movement result = movementService.createMovement(mov);

        assertEquals(400.0, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createMovement_withInsufficientFunds_shouldThrowException() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 50.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.DEBITO, -100.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));
        when(movementRepository.getLastBalanceByAccountNumber("123")).thenReturn(Optional.of(50.0));

        assertThrows(InsufficientFundsException.class, () -> movementService.createMovement(mov));
    }

    @Test
    void createMovement_withInvalidAccount_shouldThrowResourceNotFound() {
        Movement mov = new Movement(null, null, MovementType.CREDITO, 100.0, null, new Account(null, "999", null, null, null, null));

        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movementService.createMovement(mov));
    }

    @Test
    void getAllMovements_shouldReturnList() {
        Movement m1 = new Movement(1L, LocalDate.now(), MovementType.CREDITO, 100.0, 1100.0, null);
        Movement m2 = new Movement(2L, LocalDate.now(), MovementType.DEBITO, 200.0, 900.0, null);

        when(movementRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        assertEquals(2, movementService.getAllMovements().size());
    }

    @Test
    void getMovementById_shouldReturnMovement() {
        Movement m = new Movement(1L, LocalDate.now(), MovementType.CREDITO, 100.0, 600.0, null);
        when(movementRepository.findById(1L)).thenReturn(Optional.of(m));

        Optional<Movement> result = movementService.getMovementById(1L);

        assertTrue(result.isPresent());
        assertEquals(600.0, result.get().getBalance());
    }

    @Test
    void deleteMovement_shouldRemoveMovement() {
        when(movementRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movementRepository).deleteById(1L);

        movementService.deleteMovement(1L);

        verify(movementRepository).deleteById(1L);
    }

    @Test
    void createMovement_withPositiveValue_shouldIncreasBalance() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 1000.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.CREDITO, 250.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));
        when(movementRepository.getLastBalanceByAccountNumber("123")).thenReturn(Optional.of(1000.0));
        when(movementRepository.save(any(Movement.class))).thenAnswer(inv -> {
            Movement m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });

        Movement result = movementService.createMovement(mov);

        assertEquals(1250.0, result.getBalance());
    }

    @Test
    void createMovement_withNegativeValue_shouldDecreaseBalance() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 1000.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.DEBITO, -250.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));
        when(movementRepository.getLastBalanceByAccountNumber("123")).thenReturn(Optional.of(1000.0));
        when(movementRepository.save(any(Movement.class))).thenAnswer(inv -> {
            Movement m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });

        Movement result = movementService.createMovement(mov);

        assertEquals(750.0, result.getBalance());
    }

    @Test
    void createMovement_withZeroValue_shouldThrowException() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 1000.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.CREDITO, 0.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));

        assertThrows(IllegalArgumentException.class, () -> movementService.createMovement(mov));
    }

    @Test
    void createMovement_negativeValueResultingInNegativeBalance_shouldThrowException() {
        Account acc = new Account(1L, "123", com.challenge.accountservice.domain.model.AccountType.AHORROS, 100.0, true, 1L);
        Movement mov = new Movement(null, null, MovementType.DEBITO, -200.0, null, acc);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(acc));
        when(movementRepository.getLastBalanceByAccountNumber("123")).thenReturn(Optional.of(100.0));

        assertThrows(InsufficientFundsException.class, () -> movementService.createMovement(mov));
    }
}
