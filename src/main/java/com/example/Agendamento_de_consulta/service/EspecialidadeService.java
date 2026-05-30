package com.example.Agendamento_de_consulta.service;

import com.example.Agendamento_de_consulta.dto.EspecialidadeRequest;
import com.example.Agendamento_de_consulta.dto.EspecialidadeResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {
    
    private final EspecialidadeRepository especialidadeRepository;

    // LISTA TODAS AS ESPECILIDADES CADASTRADAS (RF12)
    @Transactional(readOnly = true)
    public List<EspecialidadeResponse> listarTodas() {
        return especialidadeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    // BUSCA ESPECIALIDADE PELO ID
    // CASO N ENCONTRE, RETORNA EXCEPTION HTTP 404 (RNF05)
    @Transactional(readOnly = true)
    public EspecialidadeResponse buscarPorId(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidade", id));
        return toResponse(especialidade);
    }

    // SALVA UMA NOVA ESPECIALIDADE COM VALIDAÇÕES (RF09), (RNF04)
    @Transactional
    public EspecialidadeResponse salvar(EspecialidadeRequest request) {
        // VALIDAÇÃO DE DUPLICIDADE DA ESPECIALIDADE
        if (especialidadeRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new BusinessException("Já existe uma especialidade cadastrada com este nome.");
        }

        // VALIDAÇÃO DE DUPLICIDADE DE CÓDIGO CBO
        if (especialidadeRepository.existsByCodigoCbo(request.codigoCbo())) {
            throw new BusinessException("Já existe uma especialidade cadastrada com este código CBO.");
        }

        Especialidade novaEspecialidade = new Especialidade();
        novaEspecialidade.setNome(request.nome());
        novaEspecialidade.setCodigoCbo(request.codigoCbo());

        Especialidade salva = BlackRepositorySave(novaEspecialidade);
        return toResponse(salva);

    }


    // ATUALIZAR OS DADOS DA ESPECIALIDADE
    @Transactional
    public EspecialidadeResponse atualizar(Long id, EspecialidadeRequest dadosAtualizados) {
        // Como o buscarPorId agora retorna DTO, aqui buscamos direto do repository para manipular a entidade
        Especialidade especialidadeAtual = especialidadeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidade", id));

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O NOME
        if (!especialidadeAtual.getNome().equalsIgnoreCase(dadosAtualizados.nome()) &&
                especialidadeRepository.existsByNomeIgnoreCase(dadosAtualizados.nome())) {
            throw new BusinessException("O novo nome informado já está em uso por outra especialidade.");
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE O CBO
        if (!especialidadeAtual.getCodigoCbo().equals(dadosAtualizados.codigoCbo()) &&
                especialidadeRepository.existsByCodigoCbo(dadosAtualizados.codigoCbo())) {
            throw new BusinessException("O novo código CBO informado já está em uso.");
        }

        // ATUALIZAÇÃO DOS CAMPOS
        especialidadeAtual.setNome(dadosAtualizados.nome());
        especialidadeAtual.setCodigoCbo(dadosAtualizados.codigoCbo());

        Especialidade atualizada = BlackRepositorySave(especialidadeAtual);
        return toResponse(atualizada);
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

    private EspecialidadeResponse toResponse(Especialidade especialidade) {
        return new EspecialidadeResponse(
            especialidade.getId(),
            especialidade.getNome(),
            especialidade.getCodigoCbo()
        );
    }

}
