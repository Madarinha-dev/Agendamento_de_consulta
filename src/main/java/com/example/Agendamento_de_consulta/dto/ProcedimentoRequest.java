package com.example.Agendamento_de_consulta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProcedimentoRequest (
    @NotBlank(message = "O tipo do procedimento é obrigatório")
    @Size(max = 50, message = "O tipo do produto não pode ter mais de 50 caracteres")
    String tipoProduto,

    @NotBlank(message = "O nome interno é obrigatório")
    @Size(max = 150, message = "O nome interno não pode ter mais de 150 caracteres")
    String nomeInterno,

    @Size(max = 150, message = "O nome externo não pode ter mais de 150 caracteres")
    String nomeExterno,

    @Size(max = 20, message = "O código TUSS não pode ter mais de 20 caracteres")
    String codigoTuss,

    @Size(max = 20, message = "O código CBHPM não pode ter mais de 20 caracteres")
    String codigoCbhpm,

    @Size(max = 20, message = "O código ambulatorial não pode ter mais de 20 caracteres")
    String codigoAmbulatorial,

    @Size(max = 500, message = "A observação não pode ter mais de 500 caracteres")
    String observacao,

    @NotBlank(message = "A duração da execução é obrigatória")
    @Size(max = 30, message = "A duração da execução não pode ter mais de 30 caracteres")
    String duracaoExecucao
)
{}
