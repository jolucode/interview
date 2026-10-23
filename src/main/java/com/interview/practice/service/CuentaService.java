package com.interview.practice.service;

import com.interview.practice.exception.RecursoNoEncontradoException;
import com.interview.practice.model.Cuenta;
import com.interview.practice.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CuentaService {
    private final ClienteService clienteService;
    private final CuentaRepository cuentaRepository;

    public CuentaService(ClienteService clienteService, CuentaRepository cuentaRepository) {
        this.clienteService = clienteService;
        this.cuentaRepository = cuentaRepository;
    }

    public Mono<Cuenta> obtenerCuenta(Long clienteId) {
        return clienteService.obtenerCliente(clienteId)
                // flatMap compone la consulta, que ya devuelve Mono<Cuenta>.
                .flatMap(cliente -> cuentaRepository.findByClienteId(cliente.id())
                        .switchIfEmpty(Mono.error(new RecursoNoEncontradoException(
                                "El cliente no tiene cuenta: " + clienteId))));
    }
}
