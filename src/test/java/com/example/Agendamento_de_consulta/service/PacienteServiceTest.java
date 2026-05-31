package com.example.Agendamento_de_consulta.service;

import java.time.LocalDate;
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

import com.example.Agendamento_de_consulta.dto.PacienteRequest;
import com.example.Agendamento_de_consulta.dto.PacienteResponse;
import com.example.Agendamento_de_consulta.entity.Paciente;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @InjectMocks
    private PacienteService pacienteService;

    @Mock
    private PacienteRepository pacienteRepository;

    private Paciente pacientePadrao;

    @BeforeEach
    void setUp() {
        pacientePadrao = new Paciente();
        pacientePadrao.setId(1L);
        pacientePadrao.setNome("Paciente Teste");
        pacientePadrao.setNomeSocial("Nome Social Teste");
        pacientePadrao.setCpf("12345678901");
        pacientePadrao.setTelefone("84999999999");
        pacientePadrao.setEmail("paciente@exemplo.com");
        pacientePadrao.setRg("1234567");
        pacientePadrao.setOrgaoEmissor("SSP");
        pacientePadrao.setPeso(70.5);
        pacientePadrao.setAltura(1.75);
        pacientePadrao.setNomeMae("Mãe do Paciente");
        pacientePadrao.setCep("59000000");
        pacientePadrao.setEndereco("Rua das Oliveiras");
        pacientePadrao.setComplemento("Apto 101");
        pacientePadrao.setNumero("123");
        pacientePadrao.setBairro("Centro");
        pacientePadrao.setEstado("RN");
        pacientePadrao.setCidade("Natal");
        pacientePadrao.setDataNascimento(LocalDate.of(1995, 5, 10));
        pacientePadrao.setSexo("Masculino");
        pacientePadrao.setEstadoCivil("Solteiro");
        pacientePadrao.setCartaoNacionalSaude("111222333444555");
    }

    /**
     * Utilitário para mockar o DTO PacienteRequest de forma flexível e segura no Java 25.
     */
    private PacienteRequest prepararMockRequest(String nome, String cpf, String email, String cns) {
        PacienteRequest request = mock(PacienteRequest.class);
        lenient().when(request.nome()).thenReturn(nome);
        lenient().when(request.nomeSocial()).thenReturn("Nome Social Teste");
        lenient().when(request.cpf()).thenReturn(cpf);
        lenient().when(request.telefone()).thenReturn("84999999999");
        lenient().when(request.email()).thenReturn(email);
        lenient().when(request.rg()).thenReturn("1234567");
        lenient().when(request.orgaoEmissor()).thenReturn("SSP");
        lenient().when(request.peso()).thenReturn(70.5);
        lenient().when(request.altura()).thenReturn(1.75);
        lenient().when(request.nomeMae()).thenReturn("Mãe do Paciente");
        lenient().when(request.cep()).thenReturn("59000000");
        lenient().when(request.endereco()).thenReturn("Rua das Oliveiras");
        lenient().when(request.complemento()).thenReturn("Apto 101");
        lenient().when(request.numero()).thenReturn("123");
        lenient().when(request.bairro()).thenReturn("Centro");
        lenient().when(request.estado()).thenReturn("RN");
        lenient().when(request.cidade()).thenReturn("Natal");
        lenient().when(request.dataNascimento()).thenReturn(LocalDate.of(1995, 5, 10));
        lenient().when(request.sexo()).thenReturn("Masculino");
        lenient().when(request.estadoCivil()).thenReturn("Solteiro");
        lenient().when(request.cartaoNacionalSaude()).thenReturn(cns);
        return request;
    }

    // ==========================================
    // CENÁRIOS DE LISTAGEM E BUSCA
    // ==========================================

    @Test
    @DisplayName("Deve listar todos os pacientes retornando uma lista de PacienteResponse")
    void listarTodosComSucesso() {
        when(pacienteRepository.findAll()).thenReturn(List.of(pacientePadrao));

        List<PacienteResponse> resultado = pacienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Paciente Teste", resultado.get(0).nome());
    }

    @Test
    @DisplayName("Deve buscar paciente por ID com sucesso")
    void buscarPorIdComSucesso() {
        Long idExistente = 1L;
        when(pacienteRepository.findById(idExistente)).thenReturn(Optional.of(pacientePadrao));

        PacienteResponse resultado = pacienteService.buscarPorId(idExistente);

        assertNotNull(resultado);
        assertEquals(idExistente, resultado.id());
        assertEquals("Paciente Teste", resultado.nome());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void buscarPorIdInexistente() {
        Long idInexistente = 99L;
        when(pacienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.buscarPorId(idInexistente));
    }

    // ==========================================
    // CENÁRIOS DE CADASTRO (SALVAR)
    // ==========================================

    @Test
    @DisplayName("Deve salvar um paciente com sucesso quando as regras de unicidade forem respeitadas")
    void salvarComSucesso() {
        PacienteRequest request = prepararMockRequest("Paciente Teste", "12345678901", "paciente@exemplo.com", "111222333444555");

        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(pacienteRepository.existsByEmailIgnoreCase("paciente@exemplo.com")).thenReturn(false);
        when(pacienteRepository.existsByCartaoNacionalSaude("111222333444555")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacientePadrao);

        PacienteResponse resultado = pacienteService.salvar(request);

        assertNotNull(resultado);
        assertEquals("Paciente Teste", resultado.nome());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o CPF já estiver em uso")
    void salvarComCpfDuplicado() {
        PacienteRequest request = prepararMockRequest("Paciente Teste", "12345678901", "paciente@exemplo.com", "111222333444555");

        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThrows(BusinessException.class, () -> pacienteService.salvar(request));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o E-mail já estiver em uso")
    void salvarComEmailDuplicado() {
        PacienteRequest request = prepararMockRequest("Paciente Teste", "12345678901", "paciente@exemplo.com", "111222333444555");

        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(pacienteRepository.existsByEmailIgnoreCase("paciente@exemplo.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> pacienteService.salvar(request));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao salvar se o CNS informado já estiver em uso")
    void salvarComCartaoSaudeDuplicado() {
        PacienteRequest request = prepararMockRequest("Paciente Teste", "12345678901", "paciente@exemplo.com", "111222333444555");

        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(pacienteRepository.existsByEmailIgnoreCase("paciente@exemplo.com")).thenReturn(false);
        when(pacienteRepository.existsByCartaoNacionalSaude("111222333444555")).thenReturn(true);

        assertThrows(BusinessException.class, () -> pacienteService.salvar(request));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    // ==========================================
    // CENÁRIOS DE ATUALIZAÇÃO
    // ==========================================

    @Test
    @DisplayName("Deve atualizar os dados do paciente com sucesso")
    void atualizarComSucesso() {
        Long id = 1L;
        PacienteRequest dadosNovos = prepararMockRequest("Paciente Atualizado", "12345678901", "paciente@exemplo.com", "111222333444555");

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(pacientePadrao));
        
        Paciente pacienteModificado = pacientePadrao;
        pacienteModificado.setNome("Paciente Atualizado");
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteModificado);

        PacienteResponse resultado = pacienteService.atualizar(id, dadosNovos);

        assertNotNull(resultado);
        assertEquals("Paciente Atualizado", resultado.nome());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar caso o novo CPF informado pertença a outro paciente")
    void atualizarComCpfDeOutroPaciente() {
        Long id = 1L;
        PacienteRequest dadosNovos = prepararMockRequest("Paciente Teste", "99999999999", "paciente@exemplo.com", "111222333444555");

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(pacientePadrao));
        when(pacienteRepository.existsByCpf("99999999999")).thenReturn(true);

        assertThrows(BusinessException.class, () -> pacienteService.atualizar(id, dadosNovos));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar caso o novo E-mail pertença a outro paciente")
    void atualizarComEmailDeOutroPaciente() {
        Long id = 1L;
        PacienteRequest dadosNovos = prepararMockRequest("Paciente Teste", "12345678901", "novoemail@exemplo.com", "111222333444555");

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(pacientePadrao));
        when(pacienteRepository.existsByEmailIgnoreCase("novoemail@exemplo.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> pacienteService.atualizar(id, dadosNovos));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    // ==========================================
    // CENÁRIOS DE EXCLUSÃO
    // ==========================================

    @Test
    @DisplayName("Deve deletar o paciente com sucesso quando o ID existir")
    void deletarComSucesso() {
        Long idExistente = 1L;
        when(pacienteRepository.existsById(idExistente)).thenReturn(true);

        pacienteService.deletar(idExistente);

        verify(pacienteRepository).deleteById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar um ID inexistente")
    void deletarInexistente() {
        Long idInexistente = 99L;
        when(pacienteRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.deletar(idInexistente));
        verify(pacienteRepository, never()).deleteById(idInexistente);
    }
}