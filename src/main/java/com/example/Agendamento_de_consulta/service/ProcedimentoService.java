package com.example.Agendamento_de_consulta.service;

import com.example.Agendamento_de_consulta.dto.ProcedimentoRequest;
import com.example.Agendamento_de_consulta.dto.ProcedimentoResponse;
import com.example.Agendamento_de_consulta.entity.Procedimento;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;

    // LISTA TODOS OS PRODUTOS/PROCEDIMENTOS CADASTRADOS (RF34)
    @Transactional(readOnly = true)
    public List<ProcedimentoResponse> listarTodos() {
        List<Procedimento> procedimentos = procedimentoRepository.findAll();
        return procedimentos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    // BUSCA PRODUTO/PROCEDIMENTO PELO ID
    @Transactional(readOnly = true)
    public ProcedimentoResponse buscarPorId(Long id) {
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimento", id));
        return toResponse(procedimento);
    }


    // SALVA A UM NOVO PRODUTO/PROCEDIMENTO COM VALIDAÇÕES (RF31), (RNF04)
    @Transactional
    public ProcedimentoResponse salvar(ProcedimentoRequest request) {
        
        // VALIDAÇÃO 01: EVITA DUPLICIDADE DE NOME INTERNO
        if (procedimentoRepository.existsByNomeInternoIgnoreCase(request.nomeInterno())) {
            throw new BusinessException("Já existe um procedimento cadastrado com este nome interno.");
        }

        // VALIDAÇÃO 02: EVITA DUPLICIDADE DE CÓDIGO TUSS, se for preenchido
        if (request.codigoTuss() != null && !request.codigoTuss().isBlank()) {
            if (procedimentoRepository.existsByCodigoTuss(request.codigoTuss())) {
                throw new BusinessException("Já existe um procedimento cadastrado com este código TUSS.");
            }
        }

        // VALIDAÇÃO 03: EVITA DUPLICIDADE DE CÓDIGO CBHPM, se for preenchido
        if (request.codigoCbhpm() != null && !request.codigoCbhpm().isBlank()) {
            if (procedimentoRepository.existsByCodigoCbhpm(request.codigoCbhpm())) {
                throw new BusinessException("Já existe um procedimento cadastrado com este código CBHPM.");
            }
        }

        // Converte o DTO Request para a Entidade antes de salvar
        Procedimento procedimento = new Procedimento();
        procedimento.setTipoProduto(request.tipoProduto());
        procedimento.setNomeInterno(request.nomeInterno());
        procedimento.setNomeExterno(request.nomeExterno());
        procedimento.setCodigoTuss(request.codigoTuss());
        procedimento.setCodigoCbhpm(request.codigoCbhpm());
        procedimento.setCodigoAmbulatorial(request.codigoAmbulatorial());
        procedimento.setObservacao(request.observacao());
        procedimento.setDuracaoExecucao(request.duracaoExecucao());

        Procedimento salvo = salvarNoBanco(procedimento);
        return toResponse(salvo);
    }


    // ATUALIZA OS DADOS DE UM PRODUTOR/PROCEDIMENTO EXISTENTE (RF32)
    @Transactional
    public ProcedimentoResponse atualizar(Long id, ProcedimentoRequest dadosAtualizados) {
        // Busca a entidade pura para manipulação direta
        Procedimento procedimentoAtual = procedimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimento", id));

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O NOME INTERNO
        if (!procedimentoAtual.getNomeInterno().equalsIgnoreCase(dadosAtualizados.nomeInterno()) &&
            procedimentoRepository.existsByNomeInternoIgnoreCase(dadosAtualizados.nomeInterno())) {
            throw new BusinessException("O novo nome interno informado já está em uso por outro procedimento.");
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O CÓDIGO TUSS
        if (dadosAtualizados.codigoTuss() != null && !dadosAtualizados.codigoTuss().isBlank()) {
            if (!dadosAtualizados.codigoTuss().equals(procedimentoAtual.getCodigoTuss()) &&
                procedimentoRepository.existsByCodigoTuss(dadosAtualizados.codigoTuss())) {
                throw new BusinessException("O novo código TUSS informado já está em uso.");
            }
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O CÓDIGO CBHPM
        if (dadosAtualizados.codigoCbhpm() != null && !dadosAtualizados.codigoCbhpm().isBlank()) {
            if (!dadosAtualizados.codigoCbhpm().equals(procedimentoAtual.getCodigoCbhpm()) &&
                procedimentoRepository.existsByCodigoCbhpm(dadosAtualizados.codigoCbhpm())) {
                throw new BusinessException("O novo código CBHPM informado já está em uso.");
            }
        }

        // ATUALIZAÇÃO DOS CAMPOS VINDO DO RECORD REQUEST
        procedimentoAtual.setTipoProduto(dadosAtualizados.tipoProduto());
        procedimentoAtual.setNomeInterno(dadosAtualizados.nomeInterno());
        procedimentoAtual.setNomeExterno(dadosAtualizados.nomeExterno());
        procedimentoAtual.setCodigoTuss(dadosAtualizados.codigoTuss());
        procedimentoAtual.setCodigoCbhpm(dadosAtualizados.codigoCbhpm());
        procedimentoAtual.setCodigoAmbulatorial(dadosAtualizados.codigoAmbulatorial());
        procedimentoAtual.setObservacao(dadosAtualizados.observacao());
        procedimentoAtual.setDuracaoExecucao(dadosAtualizados.duracaoExecucao());

        Procedimento atualizado = salvarNoBanco(procedimentoAtual);
        return toResponse(atualizado);
    }

    // MÉTODO DELETAR
    @Transactional
    public void deletar(Long id) {
        if (!procedimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Procedimento", id);
        }
        procedimentoRepository.deleteById(id);
    }

    private Procedimento salvarNoBanco(Procedimento procedimento) {
        return procedimentoRepository.save(procedimento);
    }


    private ProcedimentoResponse toResponse(Procedimento procedimento) {
        return new ProcedimentoResponse(
            procedimento.getId(),
            procedimento.getTipoProduto(),
            procedimento.getNomeInterno(),
            procedimento.getNomeExterno(),
            procedimento.getCodigoTuss(),
            procedimento.getCodigoCbhpm(),
            procedimento.getCodigoAmbulatorial(),
            procedimento.getObservacao(),
            procedimento.getDuracaoExecucao()
        );
    }
}
