package com.example.Agendamento_de_consulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Agendamento_de_consulta.entity.Procedimento;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long>{

    // VERIFICAÇÃO DE INTEGRIDADE (RNF04): EVITA DUPLICIDADE DE NOME INTERNO
    boolean existsByNomeInternoIgnoreCase(String nomeInterno);
    
    // VERIFICAÇÃO DE INTEGRIDADE (RNF04): EVITAR DUPLICIDADE DE CÓDIGO TUSS
    boolean existsByCodigoTuss(String codigoTuss);

    // VERIFICAÇÃO DE INTEGRIDADE (RNF04): EVITA DUPLICIDADE DE CÓDIGO CBHPM
    boolean existsByCodigoCbhpm(String codigoCbhpm);
}
