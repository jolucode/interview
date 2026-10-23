package com.interview.practice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaccion(Long id, Integer clienteId, BigDecimal monto, LocalDateTime fecha) {}
