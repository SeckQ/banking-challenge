package com.challenge.accountservice.integration;

import com.challenge.accountservice.infraestructure.messaging.cache.CustomerCache;
import com.challenge.accountservice.infraestructure.messaging.event.CustomerEvent;
import com.challenge.accountservice.infraestructure.messaging.listener.CustomerEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        // H2 in-memory database - NO PostgreSQL mode to avoid enum syntax issues
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        // Deshabilitar RabbitMQ para tests
        "spring.rabbitmq.listener.simple.auto-startup=false"
})
class CustomerEventIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerEventListener customerEventListener;

    @Autowired
    private CustomerCache customerCache;

    @BeforeEach
    void setUp() {
        // Limpiar caché antes de cada test
        customerCache.clear();
    }

    @Test
    void shouldUpdateCacheWhenCustomerCreatedEventIsReceived() {
        // Given: Un evento de cliente creado
        Long customerId = 1L;
        String customerName = "Jose Lema";
        CustomerEvent event = new CustomerEvent(customerId, customerName, "CREATED");

        // Verificar que la caché está vacía
        assertThat(customerCache.contains(customerId)).isFalse();

        // When: Invocamos el listener directamente (simula evento de RabbitMQ)
        customerEventListener.handleCustomerEvent(event);

        // Then: La caché debe actualizarse inmediatamente
        assertThat(customerCache.contains(customerId))
                .as("La caché debe contener el customerId=%d después de recibir el evento", customerId)
                .isTrue();
        assertThat(customerCache.get(customerId))
                .as("El nombre en caché debe ser '%s'", customerName)
                .isEqualTo(customerName);
    }

    @Test
    void shouldUpdateCacheWhenCustomerUpdatedEventIsReceived() {
        // Given: Cliente existe en caché
        Long customerId = 2L;
        customerCache.put(customerId, "Old Name");

        // When: Llega evento de actualización
        String newName = "Updated Name";
        CustomerEvent event = new CustomerEvent(customerId, newName, "UPDATED");
        customerEventListener.handleCustomerEvent(event);

        // Then: La caché se actualiza
        assertThat(customerCache.get(customerId)).isEqualTo(newName);
    }

    @Test
    void shouldRemoveFromCacheWhenCustomerDeletedEventIsReceived() {
        // Given: Cliente existe en caché
        Long customerId = 3L;
        customerCache.put(customerId, "Jose Lema");
        assertThat(customerCache.contains(customerId)).isTrue();

        // When: Llega evento de eliminación
        CustomerEvent event = new CustomerEvent(customerId, "Jose Lema", "DELETED");
        customerEventListener.handleCustomerEvent(event);

        // Then: El cliente se elimina de la caché
        assertThat(customerCache.contains(customerId)).isFalse();
    }


    @Test
    void shouldFailToCreateAccountWhenCustomerNotInCache() {
        // Given: Cliente NO existe en caché
        Long nonExistentCustomerId = 999L;

        // When/Then: Intentar obtener el nombre debe retornar "UNKNOWN"
        String clientName = customerCache.get(nonExistentCustomerId);
        assertThat(clientName).isEqualTo("UNKNOWN");
    }

    @Test
    void endToEndFlow_EventToHttpRequest_CreateAccount() {

        Long customerId = 500L;
        String customerName = "Pedro Martinez";
        CustomerEvent event = new CustomerEvent(customerId, customerName, "CREATED");
        
        customerEventListener.handleCustomerEvent(event);
        
        assertThat(customerCache.contains(customerId)).isTrue();
        assertThat(customerCache.get(customerId)).isEqualTo(customerName);
        
        String accountRequest = """
                {
                    "accountNumber": "ACC-500",
                    "accountType": "AHORROS",
                    "initialBalance": 3000.00,
                    "status": true,
                    "clientId": %d
                }
                """.formatted(customerId);

        webTestClient.post()
                .uri("/cuentas")
                .header("Content-Type", "application/json")
                .bodyValue(accountRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("ACC-500")
                .jsonPath("$.clientName").isEqualTo(customerName)
                .jsonPath("$.initialBalance").isEqualTo(3000.00);
        
        webTestClient.get()
                .uri("/cuentas/{id}", 1) // Primera cuenta en H2
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("ACC-500")
                .jsonPath("$.clientName").isEqualTo(customerName);
        
        String updatedName = "Pedro Martinez González";
        CustomerEvent updateEvent = new CustomerEvent(customerId, updatedName, "UPDATED");
        customerEventListener.handleCustomerEvent(updateEvent);
        
        assertThat(customerCache.get(customerId)).isEqualTo(updatedName);
        
        webTestClient.get()
                .uri("/cuentas/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("ACC-500")
                .jsonPath("$.clientName").isEqualTo(updatedName); // Nombre actualizado
    }
}
