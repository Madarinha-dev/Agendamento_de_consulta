package com.example.Agendamento_de_consulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{
    
    List<Medico> findByEspecialidades(List<Especialidade> especialidades);
}
