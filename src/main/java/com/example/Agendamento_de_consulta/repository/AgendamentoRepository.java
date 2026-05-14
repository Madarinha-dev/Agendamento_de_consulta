package com.example.Agendamento_de_consulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Agendamento_de_consulta.entity.Agendamento;
import com.example.Agendamento_de_consulta.entity.Medico;

@Repository
public interface AgendamentoRepository extends  JpaRepository<Agendamento, Long>{
    
    List<Agendamento> findByMedicoIn(List<Medico> medicos);
}
