package com.example.Agendamento_de_consulta.repository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Agendamento_de_consulta.entity.Paciente;

@DataJpaTest
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Cenário 1: Deve retornar true quando o CPF do paciente existir")
    void existsByCpfSuccess() {

        // 1. Instanciar o Paciente preenchendo todos os campos obrigatórios
        Paciente paciente = new Paciente();
        paciente.setNome("Maria Silva");
        paciente.setEmail("maria.silva@email.com");
        paciente.setCpf("98765432100");
        paciente.setTelefone("81999998888");
        paciente.setSexo("Feminino");
        paciente.setDataNascimento(LocalDate.of(1990, 5, 15));
        
        // Dados de Endereço obrigatórios
        paciente.setCep("50000000");
        paciente.setEndereco("Avenida Agamenon Magalhães");
        paciente.setNumero("123");
        paciente.setBairro("Derby");
        paciente.setCidade("Recife");
        paciente.setEstado("PE");

        // 2. Persistir no banco de dados H2
        entityManager.persist(paciente);

        // 3. Executar o Query Method que você criou
        Boolean exists = pacienteRepository.existsByCpf("98765432100");

        // 4. Asserção coerente com Boolean
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar false quando o CPF do paciente não existir")
    void existsByCpfNotFound() {
        
        // Tenta verificar um CPF que não foi cadastrado
        Boolean exists = pacienteRepository.existsByCpf("00000000000");

        // Garante que o retorno é falso
        assertThat(exists).isFalse();
    }
}