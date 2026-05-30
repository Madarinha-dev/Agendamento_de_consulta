package com.example.Agendamento_de_consulta.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Agendamento_de_consulta.entity.Agendamento;
import com.example.Agendamento_de_consulta.entity.Agendamento.StatusAgendamento;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.entity.Paciente;

@DataJpaTest
class AgendamentoRepositoryTest {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Cenário 1: Deve encontrar um agendamento por ID com sucesso")
    void findByIdSuccess() {
        
        // 1. Criar e preencher Médico
        Medico medico = new Medico();
        medico.setNome("Dr. Rodrigo");
        medico.setEmail("rodrigo@clinica.com");
        medico.setCpf("12345678900");
        medico.setTelefone("81999999999");
        medico.setTipoConselho("CRM");
        medico.setNumeroConselho("12345");
        medico.setUfConselho("PE");
        medico.setDataNascimento(LocalDate.of(1980, 5, 12));
        medico = entityManager.persist(medico);

        // 2. Criar e preencher Paciente
        Paciente paciente = new Paciente();
        paciente.setNome("Randerson Albuquerque");
        paciente.setEmail("randerson@gmail.com");
        paciente.setCpf("98765432111");
        paciente.setTelefone("81988888888");
        paciente.setSexo("MASCULINO"); 
        paciente.setDataNascimento(LocalDate.of(2000, 1, 1));
        paciente.setCep("50000000");
        paciente.setEndereco("Rua Ficticia");
        paciente.setNumero("123");
        paciente.setBairro("Boa Vista");
        paciente.setCidade("Recife");
        paciente.setEstado("PE");
        paciente = entityManager.persist(paciente);

        // 3. Criar o Agendamento preenchendo os critérios de validação estritos
        Agendamento agendamento = new Agendamento();
        agendamento.setMedico(medico);
        agendamento.setPaciente(paciente);
        
        agendamento.setDataHoraAgendamento(LocalDateTime.now().plusDays(5));
        agendamento.setDataHoraAgendaMedico(LocalDateTime.now().plusDays(5).plusHours(1));
        
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);

        // 4. Persistir o agendamento completo
        agendamento = entityManager.persist(agendamento);

        // 5. Executar a consulta do Repository
        Optional<Agendamento> result = agendamentoRepository.findById(agendamento.getId());

        // 6. Validação
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(agendamento.getId());
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar vazio ao buscar agendamento por ID inexistente")
    void findByIdNotFound() {
        Optional<Agendamento> result = agendamentoRepository.findById(999L);
        assertThat(result).isEmpty();
    }
}