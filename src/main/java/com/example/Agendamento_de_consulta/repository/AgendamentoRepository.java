package com.example.Agendamento_de_consulta.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Agendamento_de_consulta.entity.Agendamento;



@Repository
public interface AgendamentoRepository extends  JpaRepository<Agendamento, Long>{
    
    // RF29: LISTAR OS AGENDAMENTOS DOS PACIENTES DE ACORDO COM O PROFISSIONAL
    List<Agendamento> findByMedicoId(Long medicoId);


    // LISTAR AGENDAMENTO DE UM PACIENTE ESPECÍFICO
    List<Agendamento> findByPacienteId(Long pacienteId);


    // RNF08 IMPEDIR AGENDAMENTOS DUPLICADOS PARA O MESMO HORÁRIO COM O MESMO MÉDICO
    // RETONA TRUE SE O MÉDICO TIVER UMA CONSULTA ATIVA NAQUELE HORÁRIO EXATO
    boolean existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
        Long medicoId,
        LocalDateTime dataHora,
        Agendamento.StatusAgendamento status
    );


    // RNF08: IMPEDIR QUE O MESMO PACIENTE MARQUE DUAS CONSULTAS NO MESMO HORÁRIO
    boolean existsByPacienteIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
        Long pacienteId,
        LocalDateTime dataHora,
        Agendamento.StatusAgendamento status
    );

}
