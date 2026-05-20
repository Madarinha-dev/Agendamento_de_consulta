package com.example.Agendamento_de_consulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{
    
    // RF14: BUSCA MEDICO POR ESPECILIDADE
    List<Medico> findByEspecialidades(Especialidade especialidade);

    // RNF02: VALIDADOR PARA VERIFICAR SE O CPF JÁ ESTÁ CADASTRADO
    boolean existsByCpf(String cpf);

    // RNF02: VALIDADOR PARA VERIFICAR SE O E-MAIL JÁ EXISTE, IGNORANDO O FORMATO, CAIXA ALTA OU BAIXA
    boolean existsByEmailIgnoreCase(String email);

    // RNF: VALIDADOR PARA GARANTIR A INTEGRIDADE DO NÚMERO DE CONSELHO (CRM) ÚNICO
    boolean existsByNumeroConselho(String numeroConselho);
}
