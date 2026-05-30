package com.example.Agendamento_de_consulta.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Agendamento_de_consulta.dto.MedicoRequest;
import com.example.Agendamento_de_consulta.dto.MedicoResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.MedicoRepository;
import com.example.Agendamento_de_consulta.repository.EspecialidadeRepository; // INJETADO PARA BUSCAR AS ESPECIALIDADES

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicoService {
    
    private final MedicoRepository medicoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    // RF04: LISTA TODOS OS MÉDICOS DO SISTEMA
    @Transactional(readOnly = true)
    public List<MedicoResponse> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }


    // BUSCA MÉDICO PELO ID
    @Transactional(readOnly = true)
    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medico", id));
    }


    // RF14: BUSCA MÉDICO POR ESPECIALIDADE
    @Transactional(readOnly = true)
    public List<MedicoResponse> buscarPorEspecialidade(Especialidade Blacklist_especialidade) {
        return medicoRepository.findByEspecialidades(Blacklist_especialidade).stream()
                .map(this::toResponse)
                .toList();
    }


    // RF01: CADASTRO DE MÉDICOS COM VALIDAÇÕES RNF02 e RNF04
    @Transactional
    public MedicoResponse salvar(MedicoRequest request) {

        // Validação de CPF
        if (medicoRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe um médico cadastrado com este CPF.");
        }

        // Validação de E-mail, ignorando tamanho das letras
        if (medicoRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Já existe um médico com esse E-mail.");
        }

        // Validação de Número do Conselho Único 
        if (medicoRepository.existsByNumeroConselho(request.numeroConselho())) {
            throw new BusinessException("Já existe um médico cadastrado com este número de conselho.");
        }

        Medico novoMedico = new Medico();
        copiarDadosRequestParaEntidade(request, novoMedico);

        Medico medicoSalvo = medicoRepository.save(novoMedico);
        return toResponse(medicoSalvo);
    }


    // RF02: EDITAR CADASTRO MÉDICO
    @Transactional
    public MedicoResponse atualizar(Long id, MedicoRequest dadosAtualizados) {

        Medico medicoExistente = buscarPorId(id);

        // VALIDAÇÃO PARA NÃO DUPLICAR NÚMERO DO CONSELHO DE OUTRO MÉDICO
        if (!medicoExistente.getNumeroConselho().equals(dadosAtualizados.numeroConselho()) &&
            medicoRepository.existsByNumeroConselho(dadosAtualizados.numeroConselho())) {
            throw new BusinessException("O número de conselho informado já está em uso por outro médico.");
        }


        // VALIDAÇÃO PARA NÃO DUPLICAR CPF DE OUTRO MÉDICO;
        if (!medicoExistente.getCpf().equals(dadosAtualizados.cpf()) &&
            medicoRepository.existsByCpf(dadosAtualizados.cpf())) {
            throw new BusinessException("O CPF informado já está em uso por outro médico.");
        }

        // VALIDAÇÃO PARA NÃO DUPLICAR E-MAIL DE OUTRO MÉDICO;
        if (!medicoExistente.getEmail().equals(dadosAtualizados.email()) &&
            medicoRepository.existsByEmailIgnoreCase(dadosAtualizados.email())) {
            throw new BusinessException("O E-mail informado já está em uso por outro médico.");
        }
        

        // ATUALIZA OS DADOS
        try {
            copiarDadosRequestParaEntidade(dadosAtualizados, medicoExistente);
            Medico medicoAtualizado = medicoRepository.saveAndFlush(medicoExistente);
        
            MedicoResponse response = toResponse(medicoAtualizado);
            return response;

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException("Erro de integridade dos dados ao atualizar o médico. Verifique o tamanho ou duplicidade dos campos.");
        } 
    }


    // RF03: EXCLUIR MÉDICO
    @Transactional
    public void excluir(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médico", id);
        }
        medicoRepository.deleteById(id);
    }

    private void copiarDadosRequestParaEntidade(MedicoRequest request, Medico entidade) {
        entidade.setNome(request.nome());
        entidade.setCpf(request.cpf());
        entidade.setTelefone(request.telefone());
        entidade.setEmail(request.email());
        entidade.setTipoConselho(request.tipoConselho());
        entidade.setNumeroConselho(request.numeroConselho());
        entidade.setUfConselho(request.ufConselho());
        entidade.setDataNascimento(request.dataNascimento());
        entidade.setKeyConvenios(request.keyConvenios());

        // Busca as especialidades no banco de dados através dos IDs informados no DTO

        // nesse método de atualizar, o método que retornava a lista era o ".toList()",
        // essa lista ela é imutável, ai para corrigir o bug, foi necessário trocar para o ".collect()", que retorna
        // uma lista mutável, possibilitando a edição dos dados de especialidade do médico;
        List<Especialidade> especialidadesDoBanco = request.especialidadesIds().stream()
                .map(id -> especialidadeRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Especialidade", id)))
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
        entidade.setEspecialidades(especialidadesDoBanco);

    }

    private MedicoResponse toResponse(Medico entity) {
        // Converte a lista de entidades Especialidade em sub-records EspecialidadeExibicaoDTO
        List<MedicoResponse.EspecialidadeExibicaoDTO> especialidadesDto = entity.getEspecialidades().stream()
                .map(e -> new MedicoResponse.EspecialidadeExibicaoDTO(e.getId(), e.getNome(), e.getCodigoCbo()))
                .toList();

        return new MedicoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getTelefone(),
                entity.getEmail(),
                entity.getTipoConselho(),
                entity.getNumeroConselho(),
                entity.getUfConselho(),
                entity.getDataNascimento(),
                entity.getKeyConvenios(),
                especialidadesDto
        );
    }
}
