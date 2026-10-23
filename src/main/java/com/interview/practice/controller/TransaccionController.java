package com.interview.practice.controller;

import com.interview.practice.model.Transaccion;
import com.interview.practice.service.TransaccionService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@Profile("postgres")
@RequestMapping("/api/transacciones")
public class TransaccionController {
    private final TransaccionService service;

    public TransaccionController(TransaccionService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Transaccion> buscar(
            @RequestParam(name = "despuesDeId", defaultValue = "0") long despuesDeId,
            @RequestParam(name = "limite", defaultValue = "20") int limite) {
        return service.buscar(despuesDeId, limite);
    }
}
