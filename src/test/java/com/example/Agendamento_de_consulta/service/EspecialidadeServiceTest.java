package com.example.Agendamento_de_consulta.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Agendamento_de_consulta.dto.EspecialidadeRequest;
import com.example.Agendamento_de_consulta.dto.EspecialidadeResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.EspecialidadeRepository;

@ExtendWith(MockitoExtension.class)
public class EspecialidadeServiceTest {

    @InjectMocks
    private EspecialidadeService especialidadeService;

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    /**
     * Instancia o Request usando mock e lenient() para isolar o teste das assinaturas
     * mutáveis dos construtores de Records no Java 25.
     */
    private EspecialidadeRequest prepararMockRequest(String nome, String codigoCbo) {
        EspecialidadeRequest request = mock(EspecialidadeRequest.class);
        lenient().when(request.nome()).thenReturn(nome);
        lenient().when(request.codigoCbo()).thenReturn(codigoCbo);
        return request;
    }

    // ==========================================
    // CENÁRIOS DE LISTAGEM E BUSCA
    // ==========================================

    @Test
    @DisplayName("Deve retornar uma lista de EspecialidadeResponse quando houver registros")
    void listarTodasComSucesso() {
        Especialidade esp1 = new Especialidade(1L, "Cardiologia", "225120");
        Especialidade esp2 = new Especialidade(2L, "Pediatria", "225125");

        when(especialidadeRepository.findAll()).thenReturn(List.of(esp1, esp2));

        List<EspecialidadeResponse> response = especialidadeService.listarTodas();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Cardiologia", response.get(0).nome());
        assertEquals("Pediatria", response.get(1).nome());
    }

    @Test
    @DisplayName("Deve retornar EspecialidadeResponse ao buscar por um ID existente")
    void buscarPorIdComSucesso() {
        Long idExistente = 1L;
        Especialidade especialidade = new Especialidade(idExistente, "Cardiologia", "225120");

        when(especialidadeRepository.findById(idExistente)).thenReturn(Optional.of(especialidade));

        EspecialidadeResponse response = especialidadeService.buscarPorId(idExistente);

        assertNotNull(response);
        assertEquals(idExistente, response.id());
        assertEquals("Cardiologia", response.nome());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar por um ID inexistente")
    void buscarPorIdInexistente() {
        Long idInexistente = 99L;
        when(especialidadeRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> especialidadeService.buscarPorId(idInexistente));
    }

    // ==========================================
    // CENÁRIOS DE INCLUSÃO (SALVAR)
    // ==========================================

    @Test
    @DisplayName("Deve salvar uma especialidade com sucesso quando os dados forem válidos e únicos")
    void salvarComSucesso() {
        EspecialidadeRequest request = prepararMockRequest("Cardiologia", "225120");
        Especialidade especialidadeSalva = new Especialidade(1L, "Cardiologia", "225120");

        when(especialidadeRepository.existsByNomeIgnoreCase("Cardiologia")).thenReturn(false);
        when(especialidadeRepository.existsByCodigoCbo("225120")).thenReturn(false);
        when(especialidadeRepository.save(any(Especialidade.class))).thenReturn(especialidadeSalva);

        EspecialidadeResponse response = especialidadeService.salvar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(especialidadeRepository).save(any(Especialidade.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o nome da especialidade já existir")
    void salvarComNomeDuplicado() {
        EspecialidadeRequest request = prepararMockRequest("Cardiologia", "225120");

        when(especialidadeRepository.existsByNomeIgnoreCase("Cardiologia")).thenReturn(true);

        assertThrows(BusinessException.class, () -> especialidadeService.salvar(request));
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o código CBO já existir")
    void salvarComCboDuplicado() {
        EspecialidadeRequest request = prepararMockRequest("Cardiologia", "225120");

        when(especialidadeRepository.existsByNomeIgnoreCase("Cardiologia")).thenReturn(false);
        when(especialidadeRepository.existsByCodigoCbo("225120")).thenReturn(true);

        assertThrows(BusinessException.class, () -> especialidadeService.salvar(request));
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

    // ==========================================
    // CENÁRIOS DE ATUALIZAÇÃO
    // ==========================================

    @Test
    @DisplayName("Deve atualizar os dados com sucesso se os novos dados forem únicos")
    void atualizarComSucesso() {
        Long id = 1L;
        Especialidade especialidadeAtual = new Especialidade(id, "Cardiologia Antiga", "225111");
        EspecialidadeRequest dadosNovos = prepararMockRequest("Cardiologia Nova", "225120");
        Especialidade especialidadeAtualizada = new Especialidade(id, "Cardiologia Nova", "225120");

        when(especialidadeRepository.findById(id)).thenReturn(Optional.of(especialidadeAtual));
        when(especialidadeRepository.existsByNomeIgnoreCase("Cardiologia Nova")).thenReturn(false);
        when(especialidadeRepository.existsByCodigoCbo("225120")).thenReturn(false);
        when(especialidadeRepository.save(any(Especialidade.class))).thenReturn(especialidadeAtualizada);

        EspecialidadeResponse response = especialidadeService.atualizar(id, dadosNovos);

        assertNotNull(response);
        assertEquals("Cardiologia Nova", response.nome());
        assertEquals("225120", response.codigoCbo());
    }

    @Test
    @DisplayName("Deve permitir atualizar mantendo os mesmos dados antigos sem disparar erro de duplicidade")
    void atualizarMantendoMesmosDados() {
        Long id = 1L;
        Especialidade especialidadeAtual = new Especialidade(id, "Cardiologia", "225120");
        EspecialidadeRequest dadosIguais = prepararMockRequest("Cardiologia", "225120");

        when(especialidadeRepository.findById(id)).thenReturn(Optional.of(especialidadeAtual));
        when(especialidadeRepository.save(any(Especialidade.class))).thenReturn(especialidadeAtual);

        EspecialidadeResponse response = especialidadeService.atualizar(id, dadosIguais);

        assertNotNull(response);
        verify(especialidadeRepository).save(any(Especialidade.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar se o novo nome já estiver em uso por outra especialidade")
    void atualizarComNomeEmUso() {
        Long id = 1L;
        Especialidade especialidadeAtual = new Especialidade(id, "Cardiologia", "225111");
        EspecialidadeRequest dadosNovos = prepararMockRequest("Pediatria", "225111");

        when(especialidadeRepository.findById(id)).thenReturn(Optional.of(especialidadeAtual));
        when(especialidadeRepository.existsByNomeIgnoreCase("Pediatria")).thenReturn(true);

        assertThrows(BusinessException.class, () -> especialidadeService.atualizar(id, dadosNovos));
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar se o novo CBO já estiver em uso por outra especialidade")
    void atualizarComCboEmUso() {
        Long id = 1L;
        Especialidade especialidadeAtual = new Especialidade(id, "Cardiologia", "225111");
        EspecialidadeRequest dadosNovos = prepararMockRequest("Cardiologia", "225125");

        when(especialidadeRepository.findById(id)).thenReturn(Optional.of(especialidadeAtual));
        // O nome permaneceu o mesmo ("Cardiologia"), então a primeira validação passa.
        when(especialidadeRepository.existsByCodigoCbo("225125")).thenReturn(true);

        assertThrows(BusinessException.class, () -> especialidadeService.atualizar(id, dadosNovos));
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

    // ==========================================
    // CENÁRIOS DE EXCLUSÃO (DELETAR)
    // ==========================================

    @Test
    @DisplayName("Deve deletar a especialidade com sucesso quando o ID existir")
    void deletarComSucesso() {
        Long idExistente = 1L;
        when(especialidadeRepository.existsById(idExistente)).thenReturn(true);

        especialidadeService.deletar(idExistente);

        verify(especialidadeRepository).deleteById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar um ID inexistente")
    void deletarInexistente() {
        Long idInexistente = 99L;
        when(especialidadeRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> especialidadeService.deletar(idInexistente));
        verify(especialidadeRepository, never()).deleteById(idInexistente);
    }
}