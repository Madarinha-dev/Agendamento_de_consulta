package com.example.Agendamento_de_consulta.dto;

import com.example.Agendamento_de_consulta.entity.Agendamento.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AgendamentoResponse (
    Long id,
    Long medicoId,
    String nomeMedico,
    Long pacienteId,
    String nomePaciente,
    Long procedimentoId,
    Long agendaMedicoId,
    LocalDateTime dataHoraAgendaMedico,
    LocalDateTime dataHoraAgendamento,
    StatusAgendamento statusAgendamento,
    LocalDate dataNascimentoPaciente,
    String cpfPaciente,
    String telefonePaciente,
    String keyConvenioPaciente,
    String planoPaciente,
    String observacoesPaciente,
    String observacoesProcedimentoProduto
) {}
