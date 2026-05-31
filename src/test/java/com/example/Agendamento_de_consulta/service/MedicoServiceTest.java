package com.example.Agendamento_de_consulta.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.dao.DataIntegrityViolationException;

import com.example.Agendamento_de_consulta.dto.MedicoRequest;
import com.example.Agendamento_de_consulta.dto.MedicoResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.EspecialidadeRepository;
import com.example.Agendamento_de_consulta.repository.MedicoRepository;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @InjectMocks
    private MedicoService medicoService;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EspecialidadeRepository Blacklist_especialidadeRepository; // Corresponde ao especialidadeRepository do service

    private Especialidade especialidadePadrao;
    private Medico medicoPadrao;

    @BeforeEach
    void setUp() {
        especialidadePadrao = new Especialidade(1L, "Cardiologia", "225120");
        
        medicoPadrao = new Medico();
        medicoPadrao.setId(1L);
        medicoPadrao.setNome("Dr. Randerson");
        medicoPadrao.setCpf("12345678901");
        medicoPadrao.setTelefone("84999999999");
        medicoPadrao.setEmail("randerson@exemplo.com");
        medicoPadrao.setTipoConselho("CRM");
        medicoPadrao.setNumeroConselho("12345");
        medicoPadrao.setUfConselho("RN");
        medicoPadrao.setDataNascimento(LocalDate.of(1985, 10, 20));
        medicoPadrao.setKeyConvenios("Unimed, Amil");
        medicoPadrao.setEspecialidades(new ArrayList<>(List.of(especialidadePadrao)));
    }

    /**
     * Utilitário para mockar o Record de MedicoRequest blindando os testes de mudanças de construtor.
     */
    private MedicoRequest prepararMockRequest(String nome, String cpf, String email, String numeroConselho, List<Long> especialidadesIds) {
        MedicoRequest request = mock(MedicoRequest.class);
        lenient().when(request.nome()).thenReturn(nome);
        lenient().when(request.cpf()).thenReturn(cpf);
        lenient().when(request.telefone()).thenReturn("84999999999");
        lenient().when(request.email()).thenReturn(email);
        lenient().when(request.tipoConselho()).thenReturn("CRM");
        lenient().when(request.numeroConselho()).thenReturn(numeroConselho);
        lenient().when(request.ufConselho()).thenReturn("RN");
        lenient().when(request.dataNascimento()).thenReturn(LocalDate.of(1985, 10, 20));
        lenient().when(request.keyConvenios()).thenReturn("Unimed");
        lenient().when(request.especialidadesIds()).thenReturn(especialidadesIds);
        return request;
    }

    // ==========================================
    // CENÁRIOS DE LISTAGEM E BUSCA
    // ==========================================

    @Test
    @DisplayName("Deve retornar uma lista de MedicoResponse ao listar todos")
    void listarTodosComSucesso() {
        when(medicoRepository.findAll()).thenReturn(List.of(medicoPadrao));

        List<MedicoResponse> response = medicoService.listarTodos();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Dr. Randerson", response.get(0).nome());
    }

    @Test
    @DisplayName("Deve retornar a entidade Medico ao buscar por um ID existente")
    void buscarPorIdComSucesso() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoPadrao));

        Medico response = medicoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Dr. Randerson", response.getNome());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar por um ID inexistente")
    void buscarPorIdInexistente() {
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> medicoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Deve retornar médicos filtrados por especialidade")
    void buscarPorEspecialidadeComSucesso() {
        when(medicoRepository.findByEspecialidades(especialidadePadrao)).thenReturn(List.of(medicoPadrao));

        List<MedicoResponse> response = medicoService.buscarPorEspecialidade(especialidadePadrao);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Dr. Randerson", response.get(0).nome());
    }

    // ==========================================
    // CENÁRIOS DE CADASTRO (SALVAR)
    // ==========================================

    @Test
    @DisplayName("Deve salvar um médico com sucesso quando os dados forem únicos e válidos")
    void salvarComSucesso() {
        MedicoRequest request = prepararMockRequest("Dr. Randerson", "12345678901", "randerson@exemplo.com", "12345", List.of(1L));

        when(medicoRepository.existsByCpf("12345678901")).thenReturn(false);
        when(medicoRepository.existsByEmailIgnoreCase("randerson@exemplo.com")).thenReturn(false);
        when(medicoRepository.existsByNumeroConselho("12345")).thenReturn(false);
        when(Blacklist_especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidadePadrao));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoPadrao);

        MedicoResponse response = medicoService.salvar(request);

        assertNotNull(response);
        assertEquals("Dr. Randerson", response.nome());
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o CPF já estiver cadastrado")
    void salvarComCpfDuplicado() {
        MedicoRequest request = prepararMockRequest("Dr. Randerson", "12345678901", "randerson@exemplo.com", "12345", List.of(1L));

        when(medicoRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThrows(BusinessException.class, () -> medicoService.salvar(request));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o E-mail já estiver cadastrado")
    void salvarComEmailDuplicado() {
        MedicoRequest request = prepararMockRequest("Dr. Randerson", "12345678901", "randerson@exemplo.com", "12345", List.of(1L));

        when(medicoRepository.existsByCpf("12345678901")).thenReturn(false);
        when(medicoRepository.existsByEmailIgnoreCase("randerson@exemplo.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> medicoService.salvar(request));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o número de conselho já existir")
    void salvarComNumeroConselhoDuplicado() {
        MedicoRequest request = prepararMockRequest("Dr. Randerson", "12345678901", "randerson@exemplo.com", "12345", List.of(1L));

        when(medicoRepository.existsByCpf("12345678901")).thenReturn(false);
        when(medicoRepository.existsByEmailIgnoreCase("randerson@exemplo.com")).thenReturn(false);
        when(medicoRepository.existsByNumeroConselho("12345")).thenReturn(true);

        assertThrows(BusinessException.class, () -> medicoService.salvar(request));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    // ==========================================
    // CENÁRIOS DE ATUALIZAÇÃO
    // ==========================================

    @Test
    @DisplayName("Deve atualizar os dados do médico com sucesso")
    void atualizarComSucesso() {
        Long id = 1L;
        MedicoRequest dadosNovos = prepararMockRequest("Dr. Randerson Novo", "12345678901", "randerson@exemplo.com", "12345", List.of(1L));

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medicoPadrao));
        // CPF, E-mail e Conselho iguais aos do próprio médico não devem disparar exceção
        when(Blacklist_especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidadePadrao));
        
        Medico medicoAtualizado = medicoPadrao;
        medicoAtualizado.setNome("Dr. Randerson Novo");
        when(medicoRepository.saveAndFlush(any(Medico.class))).thenReturn(medicoAtualizado);

        MedicoResponse response = medicoService.atualizar(id, dadosNovos);

        assertNotNull(response);
        assertEquals("Dr. Randerson Novo", response.nome());
    }

    @Test
    @DisplayName("Deve lançar BusinessException se tentar atualizar usando o número de conselho de outro médico")
    void atualizarComConselhoDeOutroMedico() {
        Long id = 1L;
        MedicoRequest dadosNovos = prepararMockRequest("Dr. Randerson", "12345678901", "randerson@exemplo.com", "99999", List.of(1L));

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medicoPadrao));
        when(medicoRepository.existsByNumeroConselho("99999")).thenReturn(true);

        assertThrows(BusinessException.class, () -> medicoService.atualizar(id, dadosNovos));
        verify(medicoRepository, never()).saveAndFlush(any(Medico.class));
    }

    @Test
    @DisplayName("Deve capturar DataIntegrityViolationException e relançar como BusinessException")
    void atualizarComErroDeIntegridade() {
        Long id = 1L;
        MedicoRequest dadosNovos = prepararMockRequest("Dr. Nome Longo", "12345678901", "randerson@exemplo.com", "12345", List.of(1L));

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medicoPadrao));
        when(Blacklist_especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidadePadrao));
        when(medicoRepository.saveAndFlush(any(Medico.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(BusinessException.class, () -> medicoService.atualizar(id, dadosNovos));
    }

    // ==========================================
    // CENÁRIOS DE EXCLUSÃO
    // ==========================================

    @Test
    @DisplayName("Deve excluir médico com sucesso caso o ID exista")
    void excluirComSucesso() {
        Long idExistente = 1L;
        when(medicoRepository.existsById(idExistente)).thenReturn(true);

        medicoService.excluir(idExistente);

        verify(medicoRepository).deleteById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar excluir médico inexistente")
    void excluirInexistente() {
        Long idInexistente = 99L;
        when(medicoRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> medicoService.excluir(idInexistente));
        verify(medicoRepository, never()).deleteById(idInexistente);
    }
}