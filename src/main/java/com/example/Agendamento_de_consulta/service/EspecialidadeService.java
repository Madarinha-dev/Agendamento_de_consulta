package com.example.Agendamento_de_consulta.service;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {
    
    private final EspecialidadeRepository especialidadeRepository;

    // LISTA TODAS AS ESPECILIDADES CADASTRADAS (RF12)
    @Transactional(readOnly = true)
    public List<Especialidade> listarTodas() {
        return especialidadeRepository.findAll();
    }

    // BUSCA ESPECIALIDADE PELO ID
    // CASO N ENCONTRE, RETORNA EXCEPTION HTTP 404 (RNF05)
    @Transactional(readOnly = true)
    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidade", id));
    }

    // SALVA UMA NOVA ESPECIALIDADE COM VALIDAÇÕES (RF09), (RNF04)
    @Transactional
    public Especialidade salvar(Especialidade especialidade) {

        // VALIDAÇÃO DE DUPLICIDADE DA ESPECIALIDADE;
        if (especialidadeRepository.existsByNomeIgnoreCase(especialidade.getNome())) {
            throw new BusinessException("Já existe uma especialidade cadastrada com este nome.");
        }

        // VALIDAÇÃO DE DUPLICIDADE DE CÓDIGO CBO
        if (especialidadeRepository.existsByCodigoCbo(especialidade.getCodigoCbo())) {
            throw new BusinessException("Já existe uma especialidade cadastrada com este código CBO.");
        }

        return especialidadeRepository.save(especialidade);
    }


    @Transactional
    public Especialidade atualizar(Long id, Especialidade dadosAtualizados) {
        Especialidade especialidadeAtual = buscarPorId(id);

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O NOME
        if (!especialidadeAtual.getNome().equalsIgnoreCase(dadosAtualizados.getNome()) &&
                especialidadeRepository.existsByNomeIgnoreCase(dadosAtualizados.getNome())) {
            throw new BusinessException("O novo nome informado já está em uso por outra especialidade.");
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O CBO
        if (!especialidadeAtual.getCodigoCbo().equals(dadosAtualizados.getCodigoCbo()) &&
                especialidadeRepository.existsByCodigoCbo(dadosAtualizados.getCodigoCbo())) {
            throw new BusinessException("O novo código CBO informado já está em uso.");
        }

        // ATUALIZAÇÃO DOS CAMPOS APÓS AS VALIDAÇÕES
        especialidadeAtual.setNome(dadosAtualizados.getNome());
        especialidadeAtual.setCodigoCbo(dadosAtualizados.getCodigoCbo());

        return BlackRepositorySave(especialidadeAtual);
    }

    private Especialidade BlackRepositorySave(Especialidade especialidade) {
        return especialidadeRepository.save(especialidade);
    }

    

    // EXCLUIR UMA ESPECIALIDADE DO SISTEMA
    @Transactional
    public void deletar(Long id) {
        if (!especialidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Especialidade", id);
        }
        especialidadeRepository.deleteById(id);
    }

}
