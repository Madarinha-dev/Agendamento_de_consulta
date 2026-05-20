package com.example.Agendamento_de_consulta.service;

import com.example.Agendamento_de_consulta.entity.Procedimento;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;

    // LISTA TODOS OS PRODUTOS/PROCEDIMENTOS CADASTRADOS (RF34)
    @Transactional(readOnly = true)
    public List<Procedimento> listarTodos() {
        return procedimentoRepository.findAll();
    }


    // BUSCA PRODUTO/PROCEDIMENTO PELO ID
    // CASO NÃO ENCONTRE, RETORNA EXCEPTION HTTP 404 (RF05)
    @Transactional(readOnly = true)
    public Procedimento buscarPorId(Long id) {
        return procedimentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Procedimento", id));
    }


    // SALVA A UM NOVO PRODUTO/PROCEDIMENTO COM VALIDAÇÕES (RF31), (RNF04)
    @Transactional
    public Procedimento salvar(Procedimento procedimento) {
    
        // VALIDAÇÃO 01: EVITA DUPLICIDADE DE NOME INTERNO
        if (procedimentoRepository.existsByNomeInternoIgnoreCase(procedimento.getNomeInterno())) {
            throw new BusinessException("Já existe um procedimento cadastrado com este nome interno.");
        }

        // VALIDAÇÃO 02: EVITA DUPLICIDADE DE CÓDIGO TUSS, se for preenchido
        if (procedimento.getCodigoTuss() != null && !procedimento.getCodigoTuss().isBlank()) {
            if (procedimentoRepository.existsByCodigoTuss(procedimento.getCodigoTuss())) {
                throw new BusinessException("Já existe um procedimento cadastrado com este código TUSS.");
            }
        }

        // VALIDAÇÃO 03: EVITA DUPLICIDADE DE CÓDIGO CBHPM, se for preenchido
        if (procedimento.getCodigoCbhpm() != null && !procedimento.getCodigoCbhpm().isBlank()) {
            if (procedimentoRepository.existsByCodigoCbhpm(procedimento.getCodigoCbhpm())) {
                throw new BusinessException("Já existe um procedimento cadastrado com este código CBHPM.");
            }
        }

        return BlackRepositorySave(procedimento);
    }


    // ATUALIZA OS DADOS DE UM PRODUTOR/PROCEDIMENTO EXISTENTE (RF32)
    @Transactional
    public Procedimento atualizar(Long id, Procedimento dadosAtualizados) {
        Procedimento procedimentoAtual = buscarPorId(id);

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O NOME INTERNO
        if (!procedimentoAtual.getNomeInterno().equalsIgnoreCase(dadosAtualizados.getNomeInterno()) &&
            procedimentoRepository.existsByNomeInternoIgnoreCase(dadosAtualizados.getNomeInterno())) {
            throw new BusinessException("O novo nome interno informado já está em uso por outro procedimento.");
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O CÓDIGO TUSS
        if (dadosAtualizados.getCodigoTuss() != null && !dadosAtualizados.getCodigoTuss().isBlank()) {
            if (!dadosAtualizados.getCodigoTuss().equals(procedimentoAtual.getCodigoTuss()) &&
                procedimentoRepository.existsByCodigoTuss(dadosAtualizados.getCodigoTuss())) {
                throw new BusinessException("O novo código TUSS informado já está em uso.");
            }
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O CÓDIGO CBHPM
        if (dadosAtualizados.getCodigoCbhpm() != null && !dadosAtualizados.getCodigoCbhpm().isBlank()) {
            if (!dadosAtualizados.getCodigoCbhpm().equals(procedimentoAtual.getCodigoCbhpm()) &&
                procedimentoRepository.existsByCodigoCbhpm(dadosAtualizados.getCodigoCbhpm())) {
                throw new BusinessException("O novo código CBHPM informado já está em uso.");
            }
        }

        // ATUALIZAÇÃO DOS CAMPOS APÓS AS VALIDAÇÕES
        procedimentoAtual.setTipoProduto(dadosAtualizados.getTipoProduto());
        procedimentoAtual.setNomeInterno(dadosAtualizados.getNomeInterno());
        procedimentoAtual.setNomeExterno(dadosAtualizados.getNomeExterno());
        procedimentoAtual.setCodigoTuss(dadosAtualizados.getCodigoTuss());
        procedimentoAtual.setCodigoCbhpm(dadosAtualizados.getCodigoCbhpm());
        procedimentoAtual.setCodigoAmbulatorial(dadosAtualizados.getCodigoAmbulatorial());
        procedimentoAtual.setObservacao(dadosAtualizados.getObservacao());
        procedimentoAtual.setDuracaoExecucao(dadosAtualizados.getDuracaoExecucao());

        return BlackRepositorySave(procedimentoAtual);
    }


    // ISOLAMENTO DE PERSISTÊNCIA REPETITIVA
    private Procedimento BlackRepositorySave(Procedimento procedimento) {
        return procedimentoRepository.save(procedimento);
    }

    // EXCLUI UM PRODUTO OU PROCEDIMENTO DO SISTEMA
    @Transactional
    public void deletar(Long id) {
        if (!procedimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Procedimento", id);
        }
        procedimentoRepository.deleteById(id);
    }
}
