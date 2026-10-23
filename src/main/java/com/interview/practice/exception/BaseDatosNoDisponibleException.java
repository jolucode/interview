package com.interview.practice.exception;

public class BaseDatosNoDisponibleException extends RuntimeException {

    public BaseDatosNoDisponibleException(Throwable causa) {
        super("No se pudo acceder a la base de datos", causa);
    }
}
