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


    // RELACIONAMENTOS
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "procedimento_id")
    private Procedimento procedimento;



    // DADOS SOBRE MEDICO
    @Column(name = "agenda_medico_id")
    private Long agendaMedicoId;

    @Column(name = "data_hora_agenda_medico")
    private LocalDateTime dataHoraAgendaMedico;



    // DADOS SOBRE AGENDAMENTO
    @Column(name = "data_hora_agendamento", nullable = false)
    private LocalDateTime dataHoraAgendamento; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", nullable = false)
    private StatusAgendamento statusAgendamento; 



    // DADOS DO PACIENTE
    @Column(name = "data_nascimento_paciente")
    private LocalDateTime dataNascimentoPaciente; 
    
    @Column(name = "cpf_paciente")
    private String cpfPaciente; 
    
    @Column(name = "telefone_paciente")
    private String telefonePaciente; 




    // CONVÊNIOS, PLANOS E OBSERVAÇÕES TANDO DE PACIENTES QUANTO DE PROCEDIMENTO
    @Column(name = "key_convenio_paciente")
    private String keyConvenioPaciente; 
    
    @Column(name = "plano_paciente")
    private String planoPaciente; 
    
    @Column(name = "observacoes_paciente", length = 1000)
    private String observacoesPaciente; 
    
    @Column(name = "observacoes_procedimento_produto", length = 1000)
    private String observacoesProcedimentoProduto; 

    // ENUNS PARA AGENDAMENTO
    public enum StatusAgendamento {
        AGENDADO, 
        CANCELADO, 
        CONFIRMADO
    }

}