package com.example.Agendamento_de_consulta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Agendamento_de_consulta.dto.UsuarioRequest;
import com.example.Agendamento_de_consulta.dto.UsuarioResponse;
import com.example.Agendamento_de_consulta.entity.Usuario;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioPadrao;

    @BeforeEach
    void setUp() {
        usuarioPadrao = new Usuario(
            1L,
            "Carlos Silva",
            "carlos@exemplo.com",
            "Administrador",
            "12345678901",
            "senha123",
            "senha123",
            "ROLE_ADMIN"
        );
    }

    /**
     * Utilitário para mockar o Record UsuarioRequest com suporte a senhas e confirmações mutáveis.
     */
    private UsuarioRequest prepararMockRequest(String cpf, String email, String senha, String confirmacaoSenha) {
        UsuarioRequest request = mock(UsuarioRequest.class);
        lenient().when(request.nome()).thenReturn("Carlos Silva");
        lenient().when(request.email()).thenReturn(email);
        lenient().when(request.profissao()).thenReturn("Administrador");
        lenient().when(request.cpf()).thenReturn(cpf);
        lenient().when(request.senha()).thenReturn(senha);
        lenient().when(request.confirmacaoSenha()).thenReturn(confirmacaoSenha);
        lenient().when(request.permissoesAcesso()).thenReturn("ROLE_ADMIN");
        return request;
    }

    // ==========================================
    // CENÁRIOS DE LISTAGEM E BUSCA
    // ==========================================

    @Test
    @DisplayName("Deve retornar uma lista de UsuarioResponse quando houver registros cadastrados")
    void listarTodosComSucesso() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioPadrao));

        List<UsuarioResponse> response = usuarioService.listarTodos();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Carlos Silva", response.get(0).nome());
    }

    @Test
    @DisplayName("Deve retornar UsuarioResponse ao buscar por um ID existente")
    void buscarPorIdComSucesso() {
        Long idExistente = 1L;
        when(usuarioRepository.findById(idExistente)).thenReturn(Optional.of(usuarioPadrao));

        UsuarioResponse response = usuarioService.buscarPorId(idExistente);

        assertNotNull(response);
        assertEquals(idExistente, response.id());
        assertEquals("Carlos Silva", response.nome());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar por um ID inexistente")
    void buscarPorIdInexistente() {
        Long idInexistente = 99L;
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorId(idInexistente));
    }

    // ==========================================
    // CENÁRIOS DE INCLUSÃO (SALVAR)
    // ==========================================

    @Test
    @DisplayName("Deve salvar um usuário com sucesso se as credenciais e validações forem únicas")
    void salvarComSucesso() {
        UsuarioRequest request = prepararMockRequest("12345678901", "carlos@exemplo.com", "senha123", "senha123");

        when(usuarioRepository.existsByCpf("12345678901")).thenReturn(false);
        when(usuarioRepository.existsByEmailIgnoreCase("carlos@exemplo.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPadrao);

        UsuarioResponse response = usuarioService.salvar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se a senha e confirmação de senha divergirem ao salvar")
    void salvarComSenhaDivergente() {
        UsuarioRequest request = prepararMockRequest("12345678901", "carlos@exemplo.com", "senha123", "outrasenha");

        assertThrows(BusinessException.class, () -> usuarioService.salvar(request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o CPF já estiver cadastrado ao salvar")
    void salvarComCpfDuplicado() {
        UsuarioRequest request = prepararMockRequest("12345678901", "carlos@exemplo.com", "senha123", "senha123");

        when(usuarioRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThrows(BusinessException.class, () -> usuarioService.salvar(request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o E-mail já estiver cadastrado ao salvar")
    void salvarComEmailDuplicado() {
        UsuarioRequest request = prepararMockRequest("12345678901", "carlos@exemplo.com", "senha123", "senha123");

        when(usuarioRepository.existsByCpf("12345678901")).thenReturn(false);
        when(usuarioRepository.existsByEmailIgnoreCase("carlos@exemplo.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> usuarioService.salvar(request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ==========================================
    // CENÁRIOS DE ATUALIZAÇÃO
    // ==========================================

    @Test
    @DisplayName("Deve atualizar dados do usuário com sucesso se as credenciais forem válidas")
    void atualizarComSucesso() {
        Long id = 1L;
        UsuarioRequest dadosNovos = prepararMockRequest("12345678901", "carlos@exemplo.com", "novaSenha123", "novaSenha123");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioPadrao));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPadrao);

        UsuarioResponse response = usuarioService.atualizar(id, dadosNovos);

        assertNotNull(response);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve permitir atualização mantendo as senhas originais vazias/nulas")
    void atualizarMantendoSenhaOriginal() {
        Long id = 1L;
        UsuarioRequest dadosNovos = prepararMockRequest("12345678901", "carlos@exemplo.com", "", "");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioPadrao));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPadrao);

        UsuarioResponse response = usuarioService.atualizar(id, dadosNovos);

        assertNotNull(response);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se a nova senha informada e confirmação divergirem ao atualizar")
    void atualizarComSenhaDivergente() {
        Long id = 1L;
        UsuarioRequest dadosNovos = prepararMockRequest("12345678901", "carlos@exemplo.com", "novaSenha123", "senhaErrada");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioPadrao));

        assertThrows(BusinessException.class, () -> usuarioService.atualizar(id, dadosNovos));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o novo CPF já estiver sendo usado por outro usuário")
    void atualizarComCpfDeOutroUsuario() {
        Long id = 1L;
        UsuarioRequest dadosNovos = prepararMockRequest("99999999999", "carlos@exemplo.com", "", "");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioPadrao));
        when(usuarioRepository.existsByCpf("99999999999")).thenReturn(true);

        assertThrows(BusinessException.class, () -> usuarioService.atualizar(id, dadosNovos));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o novo E-mail já estiver sendo usado por outro usuário")
    void atualizarComEmailDeOutroUsuario() {
        Long id = 1L;
        UsuarioRequest dadosNovos = prepararMockRequest("12345678901", "novo@exemplo.com", "", "");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioPadrao));
        when(usuarioRepository.existsByEmailIgnoreCase("novo@exemplo.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> usuarioService.atualizar(id, dadosNovos));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ==========================================
    // CENÁRIOS DE EXCLUSÃO (DELETAR)
    // ==========================================

    @Test
    @DisplayName("Deve deletar o usuário com sucesso se o ID informado existir")
    void deletarComSucesso() {
        Long idExistente = 1L;
        when(usuarioRepository.existsById(idExistente)).thenReturn(true);

        usuarioService.deletar(idExistente);

        verify(usuarioRepository).deleteById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar um usuário inexistente")
    void deletarInexistente() {
        Long idInexistente = 99L;
        when(usuarioRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletar(idInexistente));
        verify(usuarioRepository, never()).deleteById(idInexistente);
    }
}