package com.interview.practice.service;

import com.interview.practice.model.Transaccion;
import com.interview.practice.repository.TransaccionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Profile("postgres")
public class TransaccionService {
    private final TransaccionRepository repository;

    public TransaccionService(TransaccionRepository repository) {
        this.repository = repository;
    }

    public Flux<Transaccion> buscar(long despuesDeId, int limite) {
        if (despuesDeId < 0 || limite < 1 || limite > 100) {
            return Flux.error(new IllegalArgumentException(
                    "despuesDeId debe ser >= 0 y limite debe estar entre 1 y 100"));
        }
        return repository.buscar(despuesDeId, limite);
    }
}
