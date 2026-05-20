package com.example.Agendamento_de_consulta.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicoService {
    
    private final MedicoRepository medicoRepository;

    // RF04: LISTA TODOS OS MÉDICOS DO SISTEMA
    @Transactional(readOnly = true)
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }


    // BUSCA MÉDICO PELO ID
    @Transactional(readOnly = true)
    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medico", id));
    }


    // RF14: BUSCA MÉDICO POR ESPECIALIDADE
    @Transactional(readOnly = true)
    public List<Medico> buscarPorEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidades(especialidade);
    }


    // RF01: CADASTRO DE MÉDICOS COM VALIDAÇÕES RNF02 e RNF04
    @Transactional
    public Medico salvar(Medico medico) {

        // Validação de CPF único (RNF02)
        if (medicoRepository.existsByCpf(medico.getCpf())) {
            throw new BusinessException("Já existe um médico cadastrado com este CPF.");
        }


        // Validação de E-mail único (RNF02)
        if (medicoRepository.existsByEmailIgnoreCase(medico.getEmail())) {
            throw new BusinessException("Já existe um médico com esse E-mail.");
        }


        // Validação de Número do Conselho Único (RNF04)
        if (medicoRepository.existsByNumeroConselho(medico.getNumeroConselho())) {
            throw new BusinessException("Já existe um médico cadastrado com este número de conselho.");
        }

        return medicoRepository.save(medico);
    }


    // RF02: EDITAR CADASTRO MÉDICO
    @Transactional
    public Medico atualizar(Long id, Medico dadosAtualizados) {
        Medico medicoExistente = buscarPorId(id);

        // VALIDAÇÃO PARA NÃO DUPLICAR CPF DE OUTRO MÉDICO;
        if (!medicoExistente.getCpf().equals(dadosAtualizados.getCpf()) &&
            medicoRepository.existsByCpf(dadosAtualizados.getCpf())) {
            throw new BusinessException("O CPF informado já está em uso por outro médico.");
        }


        // VALIDAÇÃO PARA NÃO DUPLICAR E-MAIL DE OUTRO MÉDICO;
        if (!medicoExistente.getEmail().equals(dadosAtualizados.getEmail()) &&
            medicoRepository.existsByEmailIgnoreCase(dadosAtualizados.getEmail())) {
            throw new BusinessException("O E-mail informado já está em uso por outro médico.");
        }

        // ATUALIZA OS DADOS DO MÉDICO
        medicoExistente.setNome(dadosAtualizados.getNome());
        medicoExistente.setCpf(dadosAtualizados.getCpf());
        medicoExistente.setTelefone(dadosAtualizados.getTelefone());
        medicoExistente.setEmail(dadosAtualizados.getEmail());
        medicoExistente.setTipoConselho(dadosAtualizados.getTipoConselho());
        medicoExistente.setNumeroConselho(dadosAtualizados.getNumeroConselho());
        medicoExistente.setUfConselho(dadosAtualizados.getUfConselho());
        medicoExistente.setDataNascimento(dadosAtualizados.getDataNascimento());
        medicoExistente.setKeyConvenios(dadosAtualizados.getKeyConvenios());

        // RF13: ASSOCIAR A ESPECIALIDADE AO CADASTRO DO MÉDICO
        medicoExistente.setEspecialidades(dadosAtualizados.getEspecialidades());
        return medicoRepository.save(medicoExistente);
    }


    // RF03: EXCLUIR MÉDICO
    @Transactional
    public void excluir(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médico", id);
        }
        medicoRepository.deleteById(id);
    }
}
