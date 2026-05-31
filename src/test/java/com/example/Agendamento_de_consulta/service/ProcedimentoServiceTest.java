package com.example.Agendamento_de_consulta.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
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

import com.example.Agendamento_de_consulta.dto.ProcedimentoRequest;
import com.example.Agendamento_de_consulta.dto.ProcedimentoResponse;
import com.example.Agendamento_de_consulta.entity.Procedimento;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.ProcedimentoRepository;

@ExtendWith(MockitoExtension.class)
public class ProcedimentoServiceTest {

    @InjectMocks
    private ProcedimentoService procedimentoService;

    @Mock
    private ProcedimentoRepository procedimentoRepository;

    private Procedimento procedimentoPadrao;

    @BeforeEach
    void setUp() {
        procedimentoPadrao = new Procedimento(
            1L, 
            "Exame", 
            "Ecocardiograma Transtorácico", 
            "Eco", 
            "40101010", 
            "101010", 
            "AMB-01", 
            "Nenhuma", 
            "30 min"
        );
    }

    /**
     * Utilitário para mockar o Record ProcedimentoRequest isolando os testes das assinaturas de Records no Java 25.
     */
    private ProcedimentoRequest prepararMockRequest(String nomeInterno, String codigoTuss, String codigoCbhpm) {
        ProcedimentoRequest request = mock(ProcedimentoRequest.class);
        lenient().when(request.tipoProduto()).thenReturn("Exame");
        lenient().when(request.nomeInterno()).thenReturn(nomeInterno);
        lenient().when(request.nomeExterno()).thenReturn("Eco");
        lenient().when(request.codigoTuss()).thenReturn(codigoTuss);
        lenient().when(request.codigoCbhpm()).thenReturn(codigoCbhpm);
        lenient().when(request.codigoAmbulatorial()).thenReturn("AMB-01");
        lenient().when(request.observacao()).thenReturn("Nenhuma");
        lenient().when(request.duracaoExecucao()).thenReturn("30 min");
        return request;
    }

    // ==========================================
    // CENÁRIOS DE LISTAGEM E BUSCA
    // ==========================================

    @Test
    @DisplayName("Deve retornar uma lista de ProcedimentoResponse quando houver registros")
    void listarTodosComSucesso() {
        when(procedimentoRepository.findAll()).thenReturn(List.of(procedimentoPadrao));

        List<ProcedimentoResponse> response = procedimentoService.listarTodos();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Ecocardiograma Transtorácico", response.get(0).nomeInterno());
    }

    @Test
    @DisplayName("Deve retornar ProcedimentoResponse ao buscar por um ID existente")
    void buscarPorIdComSucesso() {
        Long idExistente = 1L;
        when(procedimentoRepository.findById(idExistente)).thenReturn(Optional.of(procedimentoPadrao));

        ProcedimentoResponse response = procedimentoService.buscarPorId(idExistente);

        assertNotNull(response);
        assertEquals(idExistente, response.id());
        assertEquals("Ecocardiograma Transtorácico", response.nomeInterno());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar por um ID inexistente")
    void buscarPorIdInexistente() {
        Long idInexistente = 99L;
        when(procedimentoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> procedimentoService.buscarPorId(idInexistente));
    }

    // ==========================================
    // CENÁRIOS DE INCLUSÃO (SALVAR)
    // ==========================================

    @Test
    @DisplayName("Deve salvar um procedimento com sucesso quando os dados forem válidos e únicos")
    void salvarComSucesso() {
        ProcedimentoRequest request = prepararMockRequest("Ecocardiograma Transtorácico", "40101010", "101010");

        when(procedimentoRepository.existsByNomeInternoIgnoreCase("Ecocardiograma Transtorácico")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoTuss("40101010")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoCbhpm("101010")).thenReturn(false);
        when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(procedimentoPadrao);

        ProcedimentoResponse response = procedimentoService.salvar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(procedimentoRepository).save(any(Procedimento.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o nome interno já existir")
    void salvarComNomeInternoDuplicado() {
        ProcedimentoRequest request = prepararMockRequest("Ecocardiograma Transtorácico", "40101010", "101010");

        when(procedimentoRepository.existsByNomeInternoIgnoreCase("Ecocardiograma Transtorácico")).thenReturn(true);

        assertThrows(BusinessException.class, () -> procedimentoService.salvar(request));
        verify(procedimentoRepository, never()).save(any(Procedimento.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o código TUSS já existir")
    void salvarComCodigoTussDuplicado() {
        ProcedimentoRequest request = prepararMockRequest("Ecocardiograma Transtorácico", "40101010", "101010");

        when(procedimentoRepository.existsByNomeInternoIgnoreCase("Ecocardiograma Transtorácico")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoTuss("40101010")).thenReturn(true);

        assertThrows(BusinessException.class, () -> procedimentoService.salvar(request));
        verify(procedimentoRepository, never()).save(any(Procedimento.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o código CBHPM já existir")
    void salvarComCodigoCbhpmDuplicado() {
        ProcedimentoRequest request = prepararMockRequest("Ecocardiograma Transtorácico", "40101010", "101010");

        when(procedimentoRepository.existsByNomeInternoIgnoreCase("Ecocardiograma Transtorácico")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoTuss("40101010")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoCbhpm("101010")).thenReturn(true);

        assertThrows(BusinessException.class, () -> procedimentoService.salvar(request));
        verify(procedimentoRepository, never()).save(any(Procedimento.class));
    }

    // ==========================================
    // CENÁRIOS DE ATUALIZAÇÃO
    // ==========================================

    @Test
    @DisplayName("Deve atualizar os dados com sucesso se os novos dados forem únicos")
    void atualizarComSucesso() {
        Long id = 1L;
        ProcedimentoRequest dadosNovos = prepararMockRequest("Novo Nome Interno", "99999999", "888888");

        when(procedimentoRepository.findById(id)).thenReturn(Optional.of(procedimentoPadrao));
        when(procedimentoRepository.existsByNomeInternoIgnoreCase("Novo Nome Interno")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoTuss("99999999")).thenReturn(false);
        when(procedimentoRepository.existsByCodigoCbhpm("888888")).thenReturn(false);

        Procedimento procedimentoAtualizado = new Procedimento(id, "Exame", "Novo Nome Interno", "Eco", "99999999", "888888", "AMB-01", "Nenhuma", "30 min");
        when(procedimentoRepository.save(any(Procedimento.class))).thenReturn(procedimentoAtualizado);

        ProcedimentoResponse response = procedimentoService.atualizar(id, dadosNovos);

        assertNotNull(response);
        assertEquals("Novo Nome Interno", response.nomeInterno());
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar se o novo nome interno já estiver em uso por outro procedimento")
    void atualizarComNomeInternoEmUso() {
        Long id = 1L;
        ProcedimentoRequest dadosNovos = prepararMockRequest("Nome Em Uso", "40101010", "101010");

        when(procedimentoRepository.findById(id)).thenReturn(Optional.of(procedimentoPadrao));
        when(procedimentoRepository.existsByNomeInternoIgnoreCase("Nome Em Uso")).thenReturn(true);

        assertThrows(BusinessException.class, () -> procedimentoService.atualizar(id, dadosNovos));
        verify(procedimentoRepository, never()).save(any(Procedimento.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar se o novo código TUSS já estiver em uso por outro procedimento")
    void atualizarComCodigoTussEmUso() {
        Long id = 1L;
        ProcedimentoRequest dadosNovos = prepararMockRequest("Ecocardiograma Transtorácico", "TUSS-EM-USO", "101010");

        when(procedimentoRepository.findById(id)).thenReturn(Optional.of(procedimentoPadrao));
        when(procedimentoRepository.existsByCodigoTuss("TUSS-EM-USO")).thenReturn(true);

        assertThrows(BusinessException.class, () -> procedimentoService.atualizar(id, dadosNovos));
        verify(procedimentoRepository, never()).save(any(Procedimento.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar se o novo código CBHPM já estiver em uso por outro procedimento")
    void atualizarComCodigoCbhpmEmUso() {
        Long id = 1L;
        ProcedimentoRequest dadosNovos = prepararMockRequest("Ecocardiograma Transtorácico", "40101010", "CBHPM-EM-USO");

        when(procedimentoRepository.findById(id)).thenReturn(Optional.of(procedimentoPadrao));
        when(procedimentoRepository.existsByCodigoCbhpm("CBHPM-EM-USO")).thenReturn(true);

        assertThrows(BusinessException.class, () -> procedimentoService.atualizar(id, dadosNovos));
        verify(procedimentoRepository, never()).save(any(Procedimento.class));
    }

    // ==========================================
    // CENÁRIOS DE EXCLUSÃO (DELETAR)
    // ==========================================

    @Test
    @DisplayName("Deve deletar o procedimento com sucesso quando o ID existir")
    void deletarComSucesso() {
        Long idExistente = 1L;
        when(procedimentoRepository.existsById(idExistente)).thenReturn(true);

        procedimentoService.deletar(idExistente);

        verify(procedimentoRepository).deleteById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar um ID inexistente")
    void deletarInexistente() {
        Long idInexistente = 99L;
        when(procedimentoRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> procedimentoService.deletar(idInexistente));
        verify(procedimentoRepository, never()).deleteById(idInexistente);
    }
}