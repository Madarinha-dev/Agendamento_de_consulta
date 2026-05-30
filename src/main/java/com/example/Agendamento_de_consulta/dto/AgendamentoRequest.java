package com.example.Agendamento_de_consulta.dto;

import com.example.Agendamento_de_consulta.entity.Agendamento.StatusAgendamento;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record AgendamentoRequest (
    @NotNull(message = "O ID do médico é obrigatório")
    Long medicoId,

    @NotNull(message = "O ID do paciente é obrigatório")
    Long pacienteId,

    Long procedimentoId, 
    Long agendaMedicoId,

    @NotNull(message = "A data e hora da agenda do médico são obrigatórias")
    @Future(message = "O agendamento da agenda do médico deve ser para uma data e hora futura")
    LocalDateTime dataHoraAgendaMedico,

    @NotNull(message = "A data e hora do agendamento são obrigatória")
    @Future(message = "O agendamento deve ser para uma data e hora futura")
    LocalDateTime dataHoraAgendamento,

    @NotNull(message = "O status do agendamento é obrigatório")
    StatusAgendamento statusAgendamento,

    String keyConvenioPaciente,
    String planoPaciente,

    @Size(max = 1000, message = "As observações do paciente não podem exceder 1000 caracteres")
    String observacoesPaciente, 

    @Size(max = 1000, message = "As observações do procedimento não podem exceder 1000 caracteres")
    String observacoesProcedimentoProduto
) {}
