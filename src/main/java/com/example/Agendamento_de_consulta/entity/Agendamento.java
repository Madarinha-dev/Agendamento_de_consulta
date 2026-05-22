package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
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


    // RELACIONAMENTOS
    @NotNull(message = "O médico é obrigatório")
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull(message = "O paciente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "procedimento_id")
    private Procedimento procedimento;



    // DADOS SOBRE MEDICO
    @Column(name = "agenda_medico_id")
    private Long agendaMedicoId;

    @NotNull(message = "A data e hora do agendamento são obrigatórios.")
    @Future(message = "O agendamento deve ser para uma data e hora futura.")
    @Column(name = "data_hora_agenda_medico")
    private LocalDateTime dataHoraAgendaMedico;



    // DADOS SOBRE AGENDAMENTO
    @NotNull(message = "A data e hora do agendamento são obrigatórios.")
    @Future(message = "O agendamento deve ser para uma data e hora futura.")
    @Column(name = "data_hora_agendamento", nullable = false)
    private LocalDateTime dataHoraAgendamento; 

    @NotNull(message = "O status do agendamento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", nullable = false)
    private StatusAgendamento statusAgendamento; 



    // DADOS DO PACIENTE
    @Column(name = "data_nascimento_paciente")
    private LocalDate dataNascimentoPaciente; 
    
    @Column(name = "cpf_paciente")
    private String cpfPaciente; 
    
    @Column(name = "telefone_paciente")
    private String telefonePaciente; 


    
    // CONVÊNIOS, PLANOS E OBSERVAÇÕES TANDO DE PACIENTES QUANTO DE PROCEDIMENTO
    @Column(name = "key_convenio_paciente")
    private String keyConvenioPaciente; 
    
    @Column(name = "plano_paciente")
    private String planoPaciente; 
    
    @Size(max = 1000, message = "As observações do paciente não podem exceder 1000 caracteres.")
    @Column(name = "observacoes_paciente", length = 1000)
    private String observacoesPaciente; 
    
    @Size(max = 1000, message = "As observações do procedimento não podem exceder 1000 caracteres")
    @Column(name = "observacoes_procedimento_produto", length = 1000)
    private String observacoesProcedimentoProduto; 



    // ENUNS PARA AGENDAMENTO
    public enum StatusAgendamento {
        AGENDADO, 
        CANCELADO, 
        CONFIRMADO
    }

}