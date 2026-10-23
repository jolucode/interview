package com.interview.practice.repository;

import com.interview.practice.model.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteRepository {
    Flux<Cliente> findAll();
    Mono<Cliente> findById(Long id);
}
