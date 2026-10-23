package com.interview.practice.repository.postgres;

import com.interview.practice.model.Cliente;
import com.interview.practice.repository.ClienteRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Profile("postgres")
public class PostgresClienteRepository implements ClienteRepository {
    private final DatabaseClient database;

    public PostgresClienteRepository(DatabaseClient database) {
        this.database = database;
    }

    @Override
    public Flux<Cliente> findAll() {
        return database.sql("SELECT id, nombre, email FROM public.clientes ORDER BY id")
                .map((row, metadata) -> new Cliente(row.get("id", Long.class),
                        row.get("nombre", String.class), row.get("email", String.class)))
                .all();
    }

    @Override
    public Mono<Cliente> findById(Long id) {
        return database.sql("SELECT id, nombre, email FROM public.clientes WHERE id = :id")
                .bind("id", id)
                .map((row, metadata) -> new Cliente(row.get("id", Long.class),
                        row.get("nombre", String.class), row.get("email", String.class)))
                .one();
    }
}
