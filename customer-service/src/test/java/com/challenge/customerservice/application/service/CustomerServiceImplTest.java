package com.challenge.customerservice.application.service;

import com.challenge.customerservice.application.output.port.CustomerRepositoryPort;
import com.challenge.customerservice.application.service.impl.CustomerServiceImpl;
import com.challenge.customerservice.domain.model.Customer;
import com.challenge.customerservice.domain.model.Gender;
import jakarta.persistence.EntityNotFoundException;
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
class CustomerServiceImplTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer createSampleCustomer(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setIdentification("1234567890");
        customer.setName("Jose Lema");
        customer.setGender(Gender.M);
        customer.setAddress("Otavalo");
        customer.setPhone("0987");
        customer.setPassword("1234");
        customer.setStatus(true);
        return customer;
    }

    @Test
    void createCustomer_shouldReturnSavedCustomer() {
        Customer input = createSampleCustomer(null);
        Customer saved = createSampleCustomer(1L);

        when(customerRepositoryPort.findAll()).thenReturn(List.of());
        when(customerRepositoryPort.save(any())).thenReturn(saved);

        Customer result = customerService.createCustomer(input);

        assertNotNull(result.getId());
        assertEquals("Jose Lema", result.getName());
        verify(customerRepositoryPort).save(any());
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {
        Customer customer = createSampleCustomer(1L);
        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals("Jose Lema", result.get().getName());
    }

    @Test
    void getCustomerById_shouldReturnEmpty_whenNotFound() {
        when(customerRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getCustomerById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        Customer c1 = createSampleCustomer(1L);
        Customer c2 = createSampleCustomer(2L);
        c2.setName("Maria");

        when(customerRepositoryPort.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void updateCustomer_shouldWorkCorrectly() {
        Customer existing = createSampleCustomer(1L);
        Customer updated = createSampleCustomer(1L);
        updated.setName("Jose Lema Modificado");

        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepositoryPort.save(any())).thenReturn(updated);

        Customer result = customerService.updateCustomer(1L, updated);

        assertEquals("Jose Lema Modificado", result.getName());
        verify(customerRepositoryPort).save(any());
    }

    @Test
    void updateCustomer_shouldThrow_whenNotFound() {
        Customer input = createSampleCustomer(1L);

        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(1L, input));
    }

    @Test
    void deleteCustomer_shouldWorkCorrectly() {
        Customer existing = createSampleCustomer(1L);
        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(customerRepositoryPort).deleteById(1L);

        customerService.deleteCustomer(1L);

        verify(customerRepositoryPort).deleteById(1L);
    }

    @Test
    void deleteCustomer_shouldThrow_whenNotFound() {
        when(customerRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(99L));
    }

    @Test
    void updateCustomerStatus_shouldUpdateStatusSuccessfully() {
        // Given
        Customer existing = createSampleCustomer(1L);
        existing.setStatus(true);
        
        Customer updated = createSampleCustomer(1L);
        updated.setStatus(false);

        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepositoryPort.save(any())).thenReturn(updated);

        // When
        Customer result = customerService.updateCustomerStatus(1L, false);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(result.getStatus());
        verify(customerRepositoryPort).findById(1L);
        verify(customerRepositoryPort).save(any());
    }

    @Test
    void updateCustomerStatus_shouldUpdateStatusFromFalseToTrue() {
        // Given
        Customer existing = createSampleCustomer(1L);
        existing.setStatus(false);
        
        Customer updated = createSampleCustomer(1L);
        updated.setStatus(true);

        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepositoryPort.save(any())).thenReturn(updated);

        // When
        Customer result = customerService.updateCustomerStatus(1L, true);

        // Then
        assertNotNull(result);
        assertTrue(result.getStatus());
        verify(customerRepositoryPort).save(any());
    }

    @Test
    void updateCustomerStatus_shouldThrowException_whenCustomerNotFound() {
        // Given
        when(customerRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> customerService.updateCustomerStatus(99L, false)
        );

        assertEquals("Customer not found with id: 99", exception.getMessage());
        verify(customerRepositoryPort).findById(99L);
        verify(customerRepositoryPort, never()).save(any());
    }

    @Test
    void updateCustomerStatus_shouldPreserveOtherFields() {
        // Given
        Customer existing = createSampleCustomer(1L);
        existing.setStatus(true);
        existing.setName("Jose Lema");
        existing.setIdentification("1234567890");
        
        Customer savedCustomer = createSampleCustomer(1L);
        savedCustomer.setStatus(false);

        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepositoryPort.save(any())).thenReturn(savedCustomer);

        // When
        Customer result = customerService.updateCustomerStatus(1L, false);

        // Then
        assertNotNull(result);
        assertEquals("Jose Lema", result.getName());
        assertEquals("1234567890", result.getIdentification());
        assertFalse(result.getStatus());
    }
}
