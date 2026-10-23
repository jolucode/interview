package com.interview.practice.repository.memory;

import com.interview.practice.model.Cuenta;
import com.interview.practice.repository.CuentaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.Map;

@Repository
public class InMemoryCuentaRepository implements CuentaRepository {
    // Una cuenta por cliente. Luis existe, pero no tiene cuenta.
    private final Map<Long, Cuenta> cuentas = Map.of(
            1L, new Cuenta(100L, 1L, "ES001", new BigDecimal("1500.00")));

    @Override
    public Mono<Cuenta> findByClienteId(Long clienteId) {
        return Mono.defer(() -> Mono.justOrEmpty(cuentas.get(clienteId)));
    }
}
