package com.interview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
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
                .expectBody().jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    void obtieneCliente() {
        client.get().uri("/api/clientes/1").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.nombre").isEqualTo("Ana");
    }

    @Test
    void obtieneCuenta() {
        client.get().uri("/api/clientes/1/cuenta").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.numero").isEqualTo("ES001")
                .jsonPath("$.clienteId").isEqualTo(1);
    }

    @Test
    void clienteSinCuenta() {
        client.get().uri("/api/clientes/2/cuenta").exchange().expectStatus().isNotFound()
                .expectBody().jsonPath("$.detail").isEqualTo("El cliente no tiene cuenta: 2");
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
