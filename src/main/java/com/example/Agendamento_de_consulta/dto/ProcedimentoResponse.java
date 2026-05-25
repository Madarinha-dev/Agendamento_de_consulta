package com.example.Agendamento_de_consulta.dto;

public record ProcedimentoResponse (
    Long id,
    String tipoProduto,
    String nomeInterno,
    String nomeExterno,
    String codigoTuss,
    String codigoCbhpm,
    String codigoAmbulatorial,
    String observacao,
    String duracaoExecucao
) {}
