package com.example.Agendamento_de_consulta.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " não encontrado com o ID: " + id);
    }
}