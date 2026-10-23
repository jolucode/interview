package com.interview.practice.repository;

import com.interview.practice.model.Transaccion;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
@Profile("postgres")
public class TransaccionRepository {
    private final DatabaseClient database;

    public TransaccionRepository(DatabaseClient database) {
        this.database = database;
    }

    public Flux<Transaccion> buscar(long despuesDeId, int limite) {
        // Cursor por PK: evita recorrer grandes OFFSET y limita los resultados en SQL.
        return database.sql("""
                SELECT id, cliente_id, monto, fecha
                FROM public.transacciones
                WHERE id > :cursor
                ORDER BY id
                LIMIT :limite
                """)
                .bind("cursor", despuesDeId)
                .bind("limite", limite)
                .map((row, metadata) -> new Transaccion(
                        row.get("id", Long.class),
                        row.get("cliente_id", Integer.class),
                        row.get("monto", BigDecimal.class),
                        row.get("fecha", LocalDateTime.class)))
                .all();
    }
}
