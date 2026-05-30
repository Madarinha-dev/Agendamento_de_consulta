package com.example.Agendamento_de_consulta.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Agendamento_de_consulta.entity.Procedimento;

@DataJpaTest
class ProcedimentoRepositoryTest {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Cenário 1: Deve retornar true quando o nome interno do procedimento existir (ignorando maiúsculas/minúsculas)")
    void existsByNomeInternoIgnoreCaseSuccess() {
        // 1. Instanciar e preencher os campos obrigatórios da entidade Procedimento
        Procedimento procedimento = new Procedimento();
        procedimento.setNomeInterno("Exame de Sangue - Hemograma");
        procedimento.setDuracaoExecucao("00:20:00");
        procedimento.setTipoProduto("EXAME"); 
        procedimento.setNomeExterno("Hemograma Completo");
        procedimento.setObservacao("Jejum de 8 horas recomendado");

        // 2. Persistir no banco de dados H2 em memória
        entityManager.persist(procedimento);

        // 3. Executar o método real da sua interface, testando o IgnoreCase (letras minúsculas)
        boolean exists = procedimentoRepository.existsByNomeInternoIgnoreCase("exame de sangue - hemograma");

        // 4. Asserção coerente com o retorno boolean
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar false quando o nome interno do procedimento não existir")
    void existsByNomeInternoIgnoreCaseNotFound() {
        // Tenta buscar por um nome interno que sabidamente não inserimos no banco H2
        boolean exists = procedimentoRepository.existsByNomeInternoIgnoreCase("Procedimento Fantasma");

        // Garante que o retorno é falso, validando o cenário negativo
        assertThat(exists).isFalse();
    }
}