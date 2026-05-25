package com.example.Agendamento_de_consulta.dto;

public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    String profissao,
    String cpf,
    String permissoesAcesso
) {}
