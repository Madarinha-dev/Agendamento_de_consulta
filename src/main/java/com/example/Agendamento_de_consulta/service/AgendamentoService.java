package com.example.Agendamento_de_consulta.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Agendamento_de_consulta.entity.Agendamento;
import com.example.Agendamento_de_consulta.entity.Agendamento.StatusAgendamento;
import com.example.Agendamento_de_consulta.entity.Paciente;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.AgendamentoRepository;
import com.example.Agendamento_de_consulta.repository.MedicoRepository;
import com.example.Agendamento_de_consulta.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    // RF29: LISTAR OS AGENDAMENTOS DOS PACIENTES DE ACOROD COM O PROFISSIONAL QUE IRÁ ATENDER;
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorMedico(Long medicoId) {
        return agendamentoRepository.findByMedicoId(medicoId);
    }

    // LISTAR AGENDAMENTOS DE UM PACIENTE...
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorPaciente(Long pacienteId) {
        return agendamentoRepository.findByPacienteId(pacienteId);
    }

    // LISTAR TODOS OS AGENDAMENTOS...
    @Transactional(readOnly = true)
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    // LISTAR AGENDAMENTO POR ID
    @Transactional(readOnly = true)
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", id));
    }

    // RF26: Agendar o paciente de acordo com a agenda do medico
    @Transactional
    public Agendamento agendar(Agendamento agendamento) {
        
        // VALIDA SE O MÉDICO EXISTE.
        if (!medicoRepository.existsById(agendamento.getMedico().getId())) {
            throw new ResourceNotFoundException("Médico", agendamento.getMedico().getId());
        }

        // VALIDA SE O PACIENTE EXISTE, E BUSCAR OS DADOS DELE PARA ESPELHAR NA TABELA
        Paciente pacienteReal = pacienteRepository.findById(agendamento.getPaciente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", agendamento.getPaciente().getId()));

        // RNF03: Criar um validador 'horário comercial' (08h às 18h)
        validarHorarioComercial(agendamento.getDataHoraAgendamento());


        // IMPEDIR AGENDAMENTOS DUPLICADOS PARA O MESMO HORÁRIO (MÉDICO)
        boolean medicoOcupado = agendamentoRepository.existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                agendamento.getMedico().getId(),
                agendamento.getDataHoraAgendamento(),
                StatusAgendamento.CANCELADO
        );
        if (medicoOcupado) {
            throw new BusinessException("Este médico já possui um agendamento ativo para este horário.");
        }

        // RNF08: IMPEDIR QUE O MESMO PACIENTE MARQUE DUAS CONSULTAS NO MESMO HORÁRIO.
        boolean pacienteOcupado = agendamentoRepository.existsByPacienteIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                agendamento.getPaciente().getId(),
                agendamento.getDataHoraAgendamento(),
                StatusAgendamento.CANCELADO
        );
        if (pacienteOcupado) {
            throw new BusinessException("Este paciente já possui um agendamento ativo para este horário.");
        }

        // ALIMENTA OS CAMPOS OBRIGATÓRIOS PARA AGENDAMENTO. (RNF04)
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);
        agendamento.setDataHoraAgendaMedico(agendamento.getDataHoraAgendamento()); // Sincroniza a agenda
        
        // ALIMENTANDO OS CAMPOS PARA PACIENTE;
        agendamento.setCpfPaciente(pacienteReal.getCpf());
        agendamento.setTelefonePaciente(pacienteReal.getTelefone());
        agendamento.setDataNascimentoPaciente(pacienteReal.getDataNascimento());

        return agendamentoRepository.save(agendamento);
    }


    // RF27 EDITAR O AGENDAMENTO DO PACIENTE.
    @Transactional
    public Agendamento alterarAgendamento(Long id, Agendamento novosDados) {
        Agendamento agendamentoAtual = buscarPorId(id);

        if (agendamentoAtual.getStatusAgendamento() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível alterar um agendamento que já está cancelado.");
        }

        // VALIDANDO O HORÁRIO COMERCIAL
        validarHorarioComercial(novosDados.getDataHoraAgendamento());

        
        // VERIFICANDO SE O HORÁRIO (NOVO) NÃO GERA UMA DUPLICIDADE PARA O MÉDICO.
        boolean medicoOcupado = agendamentoRepository.existsByMedicoIdAndDataHoraAgendamentoAndStatusAgendamentoNot(
                agendamentoAtual.getMedico().getId(),
                novosDados.getDataHoraAgendamento(),
                StatusAgendamento.CANCELADO
        );
        // Garante que não vai se auto-bloquear caso o horário seja o mesmo
        if (medicoOcupado && !agendamentoAtual.getDataHoraAgendamento().equals(novosDados.getDataHoraAgendamento())) {
            throw new BusinessException("O médico está ocupado neste novo horário solicitado.");
        }

        agendamentoAtual.setDataHoraAgendamento(novosDados.getDataHoraAgendamento());
        agendamentoAtual.setDataHoraAgendaMedico(novosDados.getDataHoraAgendamento());
        agendamentoAtual.setObservacoesPaciente(novosDados.getObservacoesPaciente());
        agendamentoAtual.setObservacoesProcedimentoProduto(novosDados.getObservacoesProcedimentoProduto());

        return agendamentoRepository.save(agendamentoAtual);
    }

    // CANCELAR / EXCLUIR AGENDAMENTO, LIBERBANDO HORÁRIO;
    @Transactional
    public void cancelarAgendamento(Long id) {
        Agendamento agendamento = buscarPorId(id);
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
}