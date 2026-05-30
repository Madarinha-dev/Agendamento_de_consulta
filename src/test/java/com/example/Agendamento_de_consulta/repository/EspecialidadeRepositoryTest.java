package com.example.Agendamento_de_consulta.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Agendamento_de_consulta.entity.Especialidade;

@DataJpaTest
class EspecialidadeRepositoryTest {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Cenário 1: Deve encontrar uma especialidade por ID com sucesso")
    void findByIdSuccess() {

        // 1. Instanciar e preencher todos os campos obrigatórios (@NotBlank)
        Especialidade especialidade = new Especialidade();
        especialidade.setNome("Cardiologia");
        especialidade.setCodigoCbo("225120");

        // 2. Persistir no banco em memória H2
        especialidade = entityManager.persist(especialidade);

        // 3. Executar o método padrão herdado do JpaRepository
        Optional<Especialidade> result = especialidadeRepository.findById(especialidade.getId());

        // 4. Asserções
        assertThat(result).isPresent();
        assertThat(result.get().getNome()).isEqualTo("Cardiologia");
        assertThat(result.get().getCodigoCbo()).isEqualTo("225120");
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar vazio ao buscar um ID que não existe")
    void findByIdNotFound() {
        // Tenta buscar um ID aleatório alto que não foi inserido
        Optional<Especialidade> result = especialidadeRepository.findById(999L);

        // Garante que o retorno é Optional.empty()
        assertThat(result).isEmpty();
    }
}