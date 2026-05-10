package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamentos (Chaves Estrangeiras)
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // RF26: Data e hora do agendamento
    private LocalDateTime dataHoraAgendamento;

    // RF26: Status (agendado, cancelado, confirmado)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento statusAgendamento;

    // Campos de convênio e procedimentos conforme sua modelagem
    private String planoPaciente;
    private String observacoesPaciente;
    private String observacoesProcedimentoProduto;

    // Enums para garantir os status fixos
    public enum StatusAgendamento {
        AGENDADO, CANCELADO, CONFIRMADO
    }
}