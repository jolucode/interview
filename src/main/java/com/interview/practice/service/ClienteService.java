package com.interview.practice.service;

import com.interview.practice.exception.RecursoNoEncontradoException;
import com.interview.practice.model.Cliente;
import com.interview.practice.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Flux<Cliente> listarClientes() {
        return repository.findAll();
    }

    public Mono<Cliente> obtenerCliente(Long id) {
        if (id == null || id <= 0) {
            return Mono.error(new IllegalArgumentException("El ID debe ser positivo"));
        }
        return repository.findById(id).switchIfEmpty(Mono.error(
                new RecursoNoEncontradoException("No existe el cliente: " + id)));
    }
}
