package com.interview.practice.controller;

import com.interview.practice.model.Cliente;
import com.interview.practice.model.Cuenta;
import com.interview.practice.service.ClienteService;
import com.interview.practice.service.CuentaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;
    private final CuentaService cuentaService;

    public ClienteController(ClienteService clienteService, CuentaService cuentaService) {
        this.clienteService = clienteService;
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public Flux<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public Mono<Cliente> obtenerCliente(@PathVariable("id") Long id) {
        return clienteService.obtenerCliente(id);
    }

    @GetMapping("/{id}/cuenta")
    public Mono<Cuenta> obtenerCuenta(@PathVariable("id") Long id) {
        return cuentaService.obtenerCuenta(id);
    }
}
