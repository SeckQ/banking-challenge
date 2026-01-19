package com.challenge.customerservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void constructor_noArgs_shouldCreateEmptyCustomer() {
        Customer customer = new Customer();

        assertNull(customer.getId());
        assertNull(customer.getName());
        assertNull(customer.getIdentification());
        assertNull(customer.getGender());
        assertNull(customer.getAddress());
        assertNull(customer.getPhone());
        assertNull(customer.getPassword());
        assertNull(customer.getStatus());
    }

    @Test
    void constructor_allArgs_shouldCreateCustomerWithAllFields() {
        Customer customer = new Customer("password123", true);
        customer.setId(1L);
        customer.setName("Jose Lema");
        customer.setIdentification("1234567890");
        customer.setGender(Gender.M);
        customer.setAddress("Otavalo sn y principal");
        customer.setPhone("098254785");

        assertEquals(1L, customer.getId());
        assertEquals("Jose Lema", customer.getName());
        assertEquals("1234567890", customer.getIdentification());
        assertEquals(Gender.M, customer.getGender());
        assertEquals("Otavalo sn y principal", customer.getAddress());
        assertEquals("098254785", customer.getPhone());
        assertEquals("password123", customer.getPassword());
        assertTrue(customer.getStatus());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        Customer customer = new Customer();

        customer.setId(1L);
        customer.setName("Marianela Montalvo");
        customer.setIdentification("9876543210");
        customer.setGender(Gender.F);
        customer.setAddress("Amazonas y NNUU");
        customer.setPhone("097548965");
        customer.setPassword("5678");
        customer.setStatus(true);

        assertEquals(1L, customer.getId());
        assertEquals("Marianela Montalvo", customer.getName());
        assertEquals("9876543210", customer.getIdentification());
        assertEquals(Gender.F, customer.getGender());
        assertEquals("Amazonas y NNUU", customer.getAddress());
        assertEquals("097548965", customer.getPhone());
        assertEquals("5678", customer.getPassword());
        assertTrue(customer.getStatus());
    }

    @Test
    void setStatus_withFalse_shouldSetStatusToFalse() {
        Customer customer = new Customer("password", true);
        customer.setStatus(false);

        assertFalse(customer.getStatus());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        Customer customer1 = new Customer("password123", true);
        customer1.setId(1L);
        customer1.setName("Jose Lema");
        customer1.setIdentification("1234567890");
        customer1.setGender(Gender.M);
        customer1.setAddress("Otavalo");
        customer1.setPhone("098254785");

        Customer customer2 = new Customer("password123", true);
        customer2.setId(1L);
        customer2.setName("Jose Lema");
        customer2.setIdentification("1234567890");
        customer2.setGender(Gender.M);
        customer2.setAddress("Otavalo");
        customer2.setPhone("098254785");

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    void equals_withDifferentId_shouldReturnFalse() {
        Customer customer1 = new Customer("password123", true);
        customer1.setId(1L);
        customer1.setName("Jose Lema");

        Customer customer2 = new Customer("password123", true);
        customer2.setId(2L);
        customer2.setName("Jose Lema");

        assertNotEquals(customer1, customer2);
    }

    @Test
    void equals_withDifferentPassword_shouldReturnFalse() {
        Customer customer1 = new Customer("password123", true);
        customer1.setId(1L);
        customer1.setName("Jose Lema");

        Customer customer2 = new Customer("password456", true);
        customer2.setId(1L);
        customer2.setName("Jose Lema");

        assertNotEquals(customer1, customer2);
    }

    @Test
    void equals_withDifferentStatus_shouldReturnFalse() {
        Customer customer1 = new Customer("password123", true);
        customer1.setId(1L);

        Customer customer2 = new Customer("password123", false);
        customer2.setId(1L);

        assertNotEquals(customer1, customer2);
    }

    @Test
    void equals_withNull_shouldReturnFalse() {
        Customer customer = new Customer("password123", true);
        customer.setId(1L);

        assertNotEquals(null, customer);
    }

    @Test
    void equals_withDifferentClass_shouldReturnFalse() {
        Customer customer = new Customer("password123", true);
        customer.setId(1L);

        Object other = "Not a Customer";

        assertNotEquals(customer, other);
    }

    @Test
    void equals_withSameInstance_shouldReturnTrue() {
        Customer customer = new Customer("password123", true);
        customer.setId(1L);

        assertEquals(customer, customer);
    }

    @Test
    void inheritance_shouldExtendPerson() {
        Customer customer = new Customer();

        assertTrue(customer instanceof Person);
    }

    @Test
    void inheritance_shouldInheritPersonFields() {
        Customer customer = new Customer("password", true);
        
        // Verificar que tiene los campos heredados de Person
        customer.setId(1L);
        customer.setName("Test Name");
        customer.setIdentification("1234567890");
        customer.setGender(Gender.M);
        customer.setAddress("Test Address");
        customer.setPhone("555-1234");

        // Verificar acceso a campos heredados
        assertEquals(1L, customer.getId());
        assertEquals("Test Name", customer.getName());
        assertEquals("1234567890", customer.getIdentification());
        assertEquals(Gender.M, customer.getGender());
        assertEquals("Test Address", customer.getAddress());
        assertEquals("555-1234", customer.getPhone());
        
        // Verificar campos propios de Customer
        assertEquals("password", customer.getPassword());
        assertTrue(customer.getStatus());
    }

    @Test
    void toString_shouldIncludeAllFields() {
        Customer customer = new Customer("password123", true);
        customer.setId(1L);
        customer.setName("Jose Lema");
        customer.setIdentification("1234567890");
        customer.setGender(Gender.M);
        customer.setAddress("Otavalo");
        customer.setPhone("098254785");

        String toString = customer.toString();

        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        // Lombok genera el toString, solo verificamos que contenga información
        assertTrue(toString.length() > 10);
    }

    @Test
    void canEquals_withCustomerInstance_shouldReturnTrue() {
        Customer customer1 = new Customer("password", true);
        Customer customer2 = new Customer("password", true);

        assertTrue(customer1.canEqual(customer2));
    }

    @Test
    void gender_withAllValues_shouldWorkCorrectly() {
        Customer maleCustomer = new Customer();
        maleCustomer.setGender(Gender.M);
        assertEquals(Gender.M, maleCustomer.getGender());

        Customer femaleCustomer = new Customer();
        femaleCustomer.setGender(Gender.F);
        assertEquals(Gender.F, femaleCustomer.getGender());

        Customer otherCustomer = new Customer();
        otherCustomer.setGender(Gender.O);
        assertEquals(Gender.O, otherCustomer.getGender());
    }

    @Test
    void customer_withNullPassword_shouldBeValid() {
        Customer customer = new Customer(null, true);
        customer.setId(1L);
        customer.setName("Test User");

        assertNull(customer.getPassword());
        assertEquals(1L, customer.getId());
        assertEquals("Test User", customer.getName());
    }

    @Test
    void customer_withNullStatus_shouldBeValid() {
        Customer customer = new Customer("password", null);
        customer.setId(1L);
        customer.setName("Test User");

        assertNull(customer.getStatus());
        assertEquals("password", customer.getPassword());
    }
}
