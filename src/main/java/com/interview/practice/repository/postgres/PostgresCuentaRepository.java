package com.interview.practice.repository.postgres;

import com.interview.practice.model.Cuenta;
import com.interview.practice.repository.CuentaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Repository
@Profile("postgres")
public class PostgresCuentaRepository implements CuentaRepository {
    private final DatabaseClient database;

    public PostgresCuentaRepository(DatabaseClient database) {
        this.database = database;
    }

    @Override
    public Mono<Cuenta> findByClienteId(Long clienteId) {
        return database.sql("SELECT id, cliente_id, saldo FROM public.cuentas WHERE cliente_id = :clienteId")
                .bind("clienteId", clienteId)
                .map((row, metadata) -> new Cuenta(row.get("id", Long.class),
                        row.get("cliente_id", Long.class), row.get("saldo", BigDecimal.class)))
                .one();
    }
}
