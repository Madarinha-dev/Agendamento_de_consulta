package com.example.Agendamento_de_consulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Agendamento_de_consulta.entity.Especialidade;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
    
    // BUSCA A ESPECIALIDADE PELO NOME, RETORNANDO TRUE OU FALSE, PARA VALIDAÇÕES
    boolean existsByNomeIgnoreCase(String nome);

    // BUSCA A ESPECIALIDADE PELO CODIGO CBO, RETORNANDO TRUE OU FALSE, PARA VALIDAÇÕES
    boolean existsByCodigoCbo(String codigoCbo);
}
