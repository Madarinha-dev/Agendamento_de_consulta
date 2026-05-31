package com.example.Agendamento_de_consulta.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

import com.example.Agendamento_de_consulta.dto.AgendamentoRequest;
import com.example.Agendamento_de_consulta.dto.AgendamentoResponse;
import com.example.Agendamento_de_consulta.entity.Agendamento;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.entity.Paciente;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.AgendamentoRepository;
import com.example.Agendamento_de_consulta.repository.MedicoRepository;
import com.example.Agendamento_de_consulta.repository.PacienteRepository;
import com.example.Agendamento_de_consulta.repository.ProcedimentoRepository;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ProcedimentoRepository procedimentoRepository;

    private Medico medicoPadrao;
    private Paciente pacientePadrao;

    @BeforeEach
    void setUp() {
        medicoPadrao = new Medico();
        medicoPadrao.setId(1L);
        medicoPadrao.setNome("Dr. Randerson");

        pacientePadrao = new Paciente();
        pacientePadrao.setId(1L);
        pacientePadrao.setNome("Paciente Teste");
        pacientePadrao.setCpf("12345678901");
        pacientePadrao.setTelefone("84999999999");
        pacientePadrao.setDataNascimento(LocalDate.of(1995, 5, 10));
    }

    /**
     * Define as configurações como 'lenient' para que testes que interrompam
     * o fluxo precocemente não falhem por stubbings não utilizados.
     */
    private AgendamentoRequest prepararMockRequest(Long medicoId, Long pacienteId, Long procedimentoId, LocalDateTime dataHora) {
        AgendamentoRequest request = mock(AgendamentoRequest.class);
        
        lenient().when(request.medicoId()).thenReturn(medicoId);
        lenient().when(request.pacienteId()).thenReturn(pacienteId);
        lenient().when(request.procedimentoId()).thenReturn(procedimentoId); 
        lenient().when(request.dataHoraAgendamento()).thenReturn(dataHora);
        lenient().when(request.agendaMedicoId()).thenReturn(10L);
        
        lenient().when(request.keyConvenioPaciente()).thenReturn("Particular");
        lenient().when(request.planoPaciente()).thenReturn("Plano Ouro");
        lenient().when(request.observacoesPaciente()).thenReturn("Obs");
        lenient().when(request.observacoesProcedimentoProduto()).thenReturn("Obs Proc");
        
        return request;
    }

    @Test
    @DisplayName("Cenário 1: Deve salvar um agendamento com sucesso respeitando o horário comercial")
    void agendarComSucesso() {
        LocalDateTime horarioValido = LocalDateTime.of(2030, 12, 31, 10, 0); 
        AgendamentoRequest request = prepararMockRequest(1L, 1L, null, horarioValido);

        Agendamento agendamentoSalvo = new Agendamento();
        agendamentoSalvo.setId(100L);
        agendamentoSalvo.setMedico(medicoPadrao);
        agendamentoSalvo.setPaciente(pacientePadrao);
        agendamentoSalvo.setDataHoraAgendamento(horarioValido);
        agendamentoSalvo.setDataHoraAgendaMedico(horarioValido);
        agendamentoSalvo.setStatusAgendamento(Agendamento.StatusAgendamento.AGENDADO);

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoPadrao));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacientePadrao));
        
        when(agendamentoRepository.existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                1L, horarioValido, Agendamento.StatusAgendamento.CANCELADO)).thenReturn(false);
        when(agendamentoRepository.existsByPacienteIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                1L, horarioValido, Agendamento.StatusAgendamento.CANCELADO)).thenReturn(false);
        
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

        AgendamentoResponse response = agendamentoService.agendar(request);

        assertNotNull(response);
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Cenário 2: Deve lançar BusinessException ao tentar agendar fora do expediente comercial")
    void agendarForaDoHorarioComercial() {
        LocalDateTime horarioInvalido = LocalDateTime.of(2030, 12, 31, 22, 0); 
        AgendamentoRequest request = prepararMockRequest(1L, 1L, null, horarioInvalido);

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoPadrao));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacientePadrao));

        assertThrows(BusinessException.class, () -> agendamentoService.agendar(request));
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Cenário 3: Deve lançar ResourceNotFoundException quando o médico informado não existir")
    void agendarMedicoInexistente() {
        LocalDateTime horarioValido = LocalDateTime.of(2030, 12, 31, 10, 0);
        AgendamentoRequest request = prepararMockRequest(99L, 1L, null, horarioValido);

        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> agendamentoService.agendar(request));
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Cenário 4: Deve lançar BusinessException se o médico já estiver ocupado no horário")
    void agendarMedicoOcupado() {
        LocalDateTime horarioValido = LocalDateTime.of(2030, 12, 31, 14, 0);
        AgendamentoRequest request = prepararMockRequest(1L, 1L, null, horarioValido);

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoPadrao));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacientePadrao));
        
        when(agendamentoRepository.existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                1L, horarioValido, Agendamento.StatusAgendamento.CANCELADO)).thenReturn(true);

        assertThrows(BusinessException.class, () -> agendamentoService.agendar(request));
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Cenário 5: Deve cancelar um agendamento alterando o status para CANCELADO")
    void cancelarAgendamentoComSucesso() {
        Long agendamentoId = 100L;
        Agendamento agendamentoAtivo = new Agendamento();
        agendamentoAtivo.setId(agendamentoId);
        agendamentoAtivo.setStatusAgendamento(Agendamento.StatusAgendamento.AGENDADO);

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(agendamentoAtivo));

        agendamentoService.cancelarAgendamento(agendamentoId);

        verify(agendamentoRepository).save(agendamentoAtivo);
    }
}