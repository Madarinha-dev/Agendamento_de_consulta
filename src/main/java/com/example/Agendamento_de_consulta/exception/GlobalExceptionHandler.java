package com.example.Agendamento_de_consulta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // CAPTURA ERROS DE ID NÃO ENCONTRADO, PARA RETORNAR O HTTP (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }



    // CAPTURA ERROS DE REGRA DE NEGÓCIO NO SERVICES, EXEMPLO: DADOS DUPLICADOS, PARA RETORNAR O HTTP (400)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    

    // ESSE SERVE PARA CAPTURAR ERROS DE VALIDAÇÃO (@Valid), PARA RETORANR O HTTP (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String mensagemDetalahda = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        ErrorResponse erro = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), mensagemDetalahda);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}