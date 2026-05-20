package com.example.Agendamento_de_consulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Agendamento_de_consulta.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    
    // VERIFICAÇÃO DE INTEGRIDADE (RNF02/RNF04): EVITA DUPLICIDADE DE CPF
    boolean existsByCpf(String cpf);

    // VERIFICAÇÃO DE INTEGRIDADE (RNF02/RNF04): EVITA DUPLICIDADE DE EMAIL
    boolean existsByEmailIgnoreCase(String email);

    // VERIFICAÇÃO DE INTEGRIDADE (RNF04): EVITA DUPLICIDADE DE CARTÃO NACIONAL DE SAÚDE (SE ENVIADO)
    boolean existsByCartaoNacionalSaude(String cartaoNacionalSaude);
}
