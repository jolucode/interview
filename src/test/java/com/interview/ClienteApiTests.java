package com.interview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnabledIfEnvironmentVariable(named = "RUN_POSTGRES_TESTS", matches = "true")
class ClienteApiTests {
    @Autowired
    ApplicationContext context;
    WebTestClient client;

    @BeforeEach
    void configurarCliente() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void listaClientes() {
        client.get().uri("/api/clientes").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$[0].email").isEqualTo("jose@gmail.com");
    }

    @Test
    void obtieneCliente() {
        client.get().uri("/api/clientes/1").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.nombre").isEqualTo("Jose Becerra")
                .jsonPath("$.email").isEqualTo("jose@gmail.com");
    }

    @Test
    void obtieneCuenta() {
        client.get().uri("/api/clientes/1/cuenta").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.saldo").isEqualTo(15000.50)
                .jsonPath("$.clienteId").isEqualTo(1);
    }

    @Test
    void cuentasDeCarlosYMaria() {
        client.get().uri("/api/clientes/2/cuenta").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.saldo").isEqualTo(8500.00);
        client.get().uri("/api/clientes/3/cuenta").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.saldo").isEqualTo(25000.75);
    }

    @Test
    void clienteInexistente() {
        client.get().uri("/api/clientes/99/cuenta").exchange().expectStatus().isNotFound()
                .expectBody().jsonPath("$.detail").isEqualTo("No existe el cliente: 99");
    }

    @Test
    void idInvalido() {
        client.get().uri("/api/clientes/0/cuenta").exchange().expectStatus().isBadRequest();
        client.get().uri("/api/clientes/abc").exchange().expectStatus().isBadRequest();
    }
}
