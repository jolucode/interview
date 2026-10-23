package com.interview.practice.repository.memory;

import com.interview.practice.model.Cliente;
import com.interview.practice.repository.ClienteRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Repository
public class InMemoryClienteRepository implements ClienteRepository {
    private final List<Cliente> clientes = List.of(
            new Cliente(1L, "Ana"), new Cliente(2L, "Luis"));

    @Override
    public Flux<Cliente> findAll() {
        return Flux.fromIterable(clientes);
    }

    @Override
    public Mono<Cliente> findById(Long id) {
        return findAll().filter(cliente -> cliente.id().equals(id)).next();
    }
}
