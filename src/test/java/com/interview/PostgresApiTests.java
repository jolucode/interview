package com.interview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

// Prueba optativa contra la tabla existente: solo SELECT, sin modificar datos.
@SpringBootTest
@ActiveProfiles("postgres")
@EnabledIfEnvironmentVariable(named = "RUN_POSTGRES_TESTS", matches = "true")
class PostgresApiTests {
    @Autowired
    ApplicationContext context;
    WebTestClient client;

    @BeforeEach
    void configurar() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void consultaTablaExistenteYRespetaCursor() {
        client.get().uri("/api/transacciones?limite=2").exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(1);
        client.get().uri("/api/transacciones?despuesDeId=2&limite=1").exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].id").isEqualTo(3);
    }

    @Test
    void rechazaLimitesInvalidos() {
        client.get().uri("/api/transacciones?limite=101").exchange()
                .expectStatus().isBadRequest();
        client.get().uri("/api/transacciones?despuesDeId=-1").exchange()
                .expectStatus().isBadRequest();
    }
}
