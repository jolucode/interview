package com.interview.practice.repository;

import com.interview.practice.model.Cuenta;
import reactor.core.publisher.Mono;

public interface CuentaRepository {
    Mono<Cuenta> findByClienteId(Long clienteId);
}
