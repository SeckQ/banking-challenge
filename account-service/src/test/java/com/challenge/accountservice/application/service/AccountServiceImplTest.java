package com.challenge.accountservice.application.service;

import com.challenge.accountservice.application.output.port.CustomerClient;
import com.challenge.accountservice.application.output.port.AccountRepositoryPort;
import com.challenge.accountservice.application.service.impl.AccountServiceImpl;
import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account mockAccount;

    @BeforeEach
    void setUp() {
        mockAccount = new Account(
                1L,
                "123",
                AccountType.AHORROS,
                500.0,
                true,
                1L
        );
    }

    @Test
    void createAccount_shouldReturnSavedAccount_withClientName() {
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        when(customerClient.getCustomerNameById(1L)).thenReturn("Juan Perez");

        Account result = accountService.createAccount(mockAccount);

        assertNotNull(result);
        assertEquals("123", result.getAccountNumber());
        assertEquals(AccountType.AHORROS, result.getAccountType());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void getAllAccounts_shouldReturnList_withClientNames() {
        Account acc1 = new Account(1L, "111", AccountType.CORRIENTE, 1000.0, true, 1L);
        Account acc2 = new Account(2L, "222", AccountType.AHORROS, 500.0, true, 2L);

        when(accountRepository.findAll()).thenReturn(Arrays.asList(acc1, acc2));

        List<Account> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals("111", result.get(0).getAccountNumber());
        assertEquals("222", result.get(1).getAccountNumber());
    }

    @Test
    void getAccountById_shouldReturnAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

        Optional<Account> resultOpt = accountService.getAccountById(1L);
        assertTrue(resultOpt.isPresent());
        Account result = resultOpt.get();

        assertNotNull(result);
        assertEquals("123", result.getAccountNumber());
    }

    @Test
    void updateAccount_shouldModifyAndReturnUpdatedAccount() {
        Account updatedAccount = new Account(1L, "ABC123", AccountType.CORRIENTE, 1500.0, true, 5L);

        when(accountRepository.existsById(1L)).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(1L, updatedAccount);

        assertNotNull(result);
        assertEquals("ABC123", result.getAccountNumber());
        assertEquals(1500.0, result.getInitialBalance());
    }

    @Test
    void deleteAccount_shouldRemoveAccount() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        doNothing().when(accountRepository).deleteById(1L);

        accountService.deleteAccount(1L);

        verify(accountRepository).deleteById(1L);
    }

    @Test
    void updateAccountStatus_shouldChangeStatusToFalse() {
        Account accountWithNewStatus = new Account(1L, "123", AccountType.AHORROS, 500.0, false, 1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithNewStatus);

        Account result = accountService.updateAccountStatus(1L, false);

        assertNotNull(result);
        assertEquals(false, result.getStatus());
        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void updateAccountStatus_shouldChangeStatusToTrue() {
        Account inactiveAccount = new Account(1L, "123", AccountType.AHORROS, 500.0, false, 1L);
        Account accountWithNewStatus = new Account(1L, "123", AccountType.AHORROS, 500.0, true, 1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(inactiveAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithNewStatus);

        Account result = accountService.updateAccountStatus(1L, true);

        assertNotNull(result);
        assertEquals(true, result.getStatus());
        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void updateAccountStatus_shouldThrowException_whenAccountNotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(com.challenge.accountservice.infraestructure.exception.ResourceNotFoundException.class,
                () -> accountService.updateAccountStatus(999L, false));

        verify(accountRepository).findById(999L);
        verify(accountRepository, never()).save(any(Account.class));
    }
}
