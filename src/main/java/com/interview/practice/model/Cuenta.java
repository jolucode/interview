package com.interview.practice.model;

import java.math.BigDecimal;

public record Cuenta(Long id, Long clienteId, String numero, BigDecimal saldo) {}
