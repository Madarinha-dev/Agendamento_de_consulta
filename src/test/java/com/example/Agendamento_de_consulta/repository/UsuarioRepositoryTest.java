package com.example.Agendamento_de_consulta.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Agendamento_de_consulta.entity.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Cenário 1: Deve buscar usuário por email com sucesso")
    void findByEmailSuccess() {
        
        // 1. Instanciar e preencher todos os campos obrigatórios da entidade Usuario
        Usuario usuario = new Usuario();
        usuario.setNome("Randerson Medeiros");
        usuario.setEmail("randerson@clinica.com");
        usuario.setCpf("12345678901");
        usuario.setSenha("senha123");
        usuario.setPermissoesAcesso("ADMIN");
        usuario.setProfissao("Desenvolvedor");

        // 2. Persistir no banco de dados H2 em memória
        entityManager.persist(usuario);

        // 3. Executar o Query Method customizado do seu UsuarioRepository
        Optional<Usuario> result = usuarioRepository.findByEmail("randerson@clinica.com");

        // 4. Asserções
        assertThat(result).isPresent();
        assertThat(result.get().getNome()).isEqualTo("Randerson Medeiros");
        assertThat(result.get().getCpf()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar vazio ao buscar por um email inexistente")
    void findByEmailNotFound() {

        // Tenta buscar por um email que sabidamente não foi cadastrado no banco H2
        Optional<Usuario> result = usuarioRepository.findByEmail("inexistente@clinica.com");

        // Garante que o retorno é um Optional vazio, cobrindo o cenário de erro/ausência
        assertThat(result).isEmpty();
    }
}