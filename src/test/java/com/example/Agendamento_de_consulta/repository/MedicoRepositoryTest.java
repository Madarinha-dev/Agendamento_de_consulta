package com.example.Agendamento_de_consulta.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;

@DataJpaTest
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Cenário 1: Deve buscar médicos por especialidade com sucesso")
    void findByEspecialidadesSuccess() {
        // 1. Criar e persistir a Especialidade obrigatória primeiro
        Especialidade especialidade = new Especialidade();
        especialidade.setNome("Pediatria");
        especialidade.setCodigoCbo("225125");
        especialidade = entityManager.persist(especialidade);

        // 2. Criar e preencher a entidade Medico com todas as validações obrigatórias
        Medico medico = new Medico();
        medico.setNome("Dr. Augusto");
        medico.setEmail("augusto@clinica.com");
        medico.setCpf("12345678911");
        medico.setTelefone("81988887777");
        medico.setTipoConselho("CRM");
        medico.setNumeroConselho("54321");
        medico.setUfConselho("PE");
        medico.setDataNascimento(LocalDate.of(1975, 8, 20));

        // Associar a especialidade persistida ao médico
        List<Especialidade> listaEspecialidades = new ArrayList<>();
        listaEspecialidades.add(especialidade);
        medico.setEspecialidades(listaEspecialidades);

        // Salvar o médico completo no banco H2
        entityManager.persist(medico);

        // 3. Executar a consulta usando a especialidade persistida que possui ID válido
        List<Medico> result = medicoRepository.findByEspecialidades(especialidade);

        // 4. Asserções
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNome()).isEqualTo("Dr. Augusto");
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar vazio ao buscar um médico por ID inexistente")
    void findByIdNotFound() {
        // Tenta buscar um médico por um ID arbitrário que não foi inserido
        Optional<Medico> result = medicoRepository.findById(999L);

        // Garante que o retorno é vazio, validando a integridade das buscas
        assertThat(result).isEmpty();
    }
}