package com.example.Agendamento_de_consulta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Agendamento_de_consulta.dto.AgendamentoRequest;
import com.example.Agendamento_de_consulta.dto.AgendamentoResponse;
import com.example.Agendamento_de_consulta.entity.Agendamento;
import com.example.Agendamento_de_consulta.entity.Agendamento.StatusAgendamento;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.entity.Paciente;
import com.example.Agendamento_de_consulta.entity.Procedimento;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.AgendamentoRepository;
import com.example.Agendamento_de_consulta.repository.MedicoRepository;
import com.example.Agendamento_de_consulta.repository.PacienteRepository;
import com.example.Agendamento_de_consulta.repository.ProcedimentoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimentoRepository procedimentoRepository;

    // [ GET ] - LISTA OS AGENDAMENTOS DOS PACIENTES DE ACORDO COM O PROFISSIONAL
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorMedico(Long medicoId) {
        // VALIDA SE O MÉDICO EXISTE
        if (!medicoRepository.existsById(medicoId)) {
            throw new ResourceNotFoundException("Médico", medicoId);
        }
        return agendamentoRepository.findByMedicoId(medicoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    // [ GET ] - LISTA AGENDAMENTOS DE UM PACIENTE
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPaciente(Long pacienteId) {
        // VALIDA SE O PACIENTE EXISTE
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new ResourceNotFoundException("Paciente", pacienteId);
        }
        return agendamentoRepository.findByPacienteId(pacienteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    
    // [ GET ] - LISTA TODOS OS AGENDAMENTOS
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    
    // [ GET ] - LISTA AGENDAMENTO POR ID
    @Transactional(readOnly = true)
    public Agendamento buscarEntidadePorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", id));
    }


    // [ GET ] - BUSCA POR ID
    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }


    // [ POST ] - AGENDA O PACIENTE DE ACORDO COM A AGENDA DO MÉDICO
    @Transactional
    public AgendamentoResponse agendar(AgendamentoRequest request) {
        
        // VALIDA SE O MÉDICO EXISTE E RECUPERA A ENTIDADE
        Medico medicoReal = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico", request.medicoId()));

        // VALIDA SE O PACIENTE EXISTE E RECUPERA OS DADOS PARA ESPELHAMENTO
        Paciente pacienteReal = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", request.pacienteId()));

        // TRATAMENTO OPCIONAL DO PROCEDIMENTO
        Procedimento procedimentoReal = null;
        if (request.procedimentoId() != null) {
            procedimentoReal = procedimentoRepository.findById(request.procedimentoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Procedimento", request.procedimentoId()));
        }

        // Validador 'horário comercial' (08h às 18h)
        validarHorarioComercial(request.dataHoraAgendamento());

        // IMPEDIR AGENDAMENTOS DUPLICADOS PARA O MESMO HORÁRIO (MÉDICO)
        boolean medicoOcupado = agendamentoRepository.existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                request.medicoId(),
                request.dataHoraAgendamento(),
                StatusAgendamento.CANCELADO
        );
        if (medicoOcupado) {
            throw new BusinessException("Este médico já possui um agendamento ativo para este horário.");
        }

        // IMPEDIR QUE O MESMO PACIENTE MARQUE DUAS CONSULTAS NO MESMO HORÁRIO
        boolean pacienteOcupado = agendamentoRepository.existsByPacienteIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                request.pacienteId(),
                request.dataHoraAgendamento(),
                StatusAgendamento.CANCELADO
        );
        if (pacienteOcupado) {
            throw new BusinessException("Este paciente já possui um agendamento ativo para este horário.");
        }

        
        Agendamento agendamento = new Agendamento();
        agendamento.setMedico(medicoReal);
        agendamento.setPaciente(pacienteReal);
        agendamento.setProcedimento(procedimentoReal);
        agendamento.setAgendaMedicoId(request.agendaMedicoId());
        agendamento.setDataHoraAgendamento(request.dataHoraAgendamento());
        agendamento.setDataHoraAgendaMedico(request.dataHoraAgendamento()); 
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);
        
        
        agendamento.setCpfPaciente(pacienteReal.getCpf());
        agendamento.setTelefonePaciente(pacienteReal.getTelefone());
        agendamento.setDataNascimentoPaciente(pacienteReal.getDataNascimento());

        
        agendamento.setKeyConvenioPaciente(request.keyConvenioPaciente());
        agendamento.setPlanoPaciente(request.planoPaciente());
        agendamento.setObservacoesPaciente(request.observacoesPaciente());
        agendamento.setObservacoesProcedimentoProduto(request.observacoesProcedimentoProduto());

        return toResponse(agendamentoRepository.save(agendamento));
    }


    // [ PUT ] - EDITA O AGENDAMENTO DO PACIENTE
    @Transactional
    public AgendamentoResponse alterarAgendamento(Long id, AgendamentoRequest request) {
        Agendamento agendamentoAtual = buscarEntidadePorId(id);

        if (agendamentoAtual.getStatusAgendamento() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível alterar um agendamento que já está cancelado.");
        }

        // VALIDANDO O HORÁRIO COMERCIAL
        validarHorarioComercial(request.dataHoraAgendamento());

        // VERIFICANDO DUPLICIDADE DO MÉDICO (GARANTE NÃO SE BLOQUEAR)
        boolean medicoOcupado = agendamentoRepository.existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                agendamentoAtual.getMedico().getId(),
                request.dataHoraAgendamento(),
                StatusAgendamento.CANCELADO
        );

        if (medicoOcupado && !agendamentoAtual.getDataHoraAgendamento().equals(request.dataHoraAgendamento())) {
            throw new BusinessException("O médico está ocupado neste horário solicitado.");
        }

        // ATUALIZAÇÃO DO PROCEDIMENTO 
        if (request.procedimentoId() != null) {
            Procedimento novoProcedimento = procedimentoRepository.findById(request.procedimentoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Procedimento", request.procedimentoId()));
            agendamentoAtual.setProcedimento(novoProcedimento);
        } else {
            agendamentoAtual.setProcedimento(null);
        }

        agendamentoAtual.setDataHoraAgendamento(request.dataHoraAgendamento());
        agendamentoAtual.setDataHoraAgendaMedico(request.dataHoraAgendamento());
        agendamentoAtual.setObservacoesPaciente(request.observacoesPaciente());
        agendamentoAtual.setObservacoesProcedimentoProduto(request.observacoesProcedimentoProduto());

        return toResponse(agendamentoRepository.save(agendamentoAtual));
    }


    // CANCELAR / EXCLUIR AGENDAMENTO, LIBERBANDO HORÁRIO;
    @Transactional
    public void cancelarAgendamento(Long id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
    }


    // MÉTODO AUXÍLIA, PARA VALIDAR O HORÁRIO COMERCIAL.
    private void validarHorarioComercial(LocalDateTime dataHora) {
        int hora = dataHora.getHour();
        if (hora < 8 || hora >= 18) {
            throw new BusinessException("Horário fora do expediente comercial. Consultas permitidas apenas entre 08h e 18h.");
        }
    }


    private AgendamentoResponse toResponse(Agendamento entity) {
        return new AgendamentoResponse(
                entity.getId(),
                entity.getMedico().getId(),
                entity.getMedico().getNome(),
                entity.getPaciente().getId(),
                entity.getPaciente().getNome(),
                entity.getProcedimento() != null ? entity.getProcedimento().getId() : null,
                entity.getAgendaMedicoId(),
                entity.getDataHoraAgendaMedico(),
                entity.getDataHoraAgendamento(),
                entity.getStatusAgendamento(),
                entity.getDataNascimentoPaciente(),
                entity.getCpfPaciente(),
                entity.getTelefonePaciente(),
                entity.getKeyConvenioPaciente(),
                entity.getPlanoPaciente(),
                entity.getObservacoesPaciente(),
                entity.getObservacoesProcedimentoProduto()
        );
    }
}