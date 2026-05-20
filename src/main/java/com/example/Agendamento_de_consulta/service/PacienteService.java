package com.example.Agendamento_de_consulta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.example.Agendamento_de_consulta.entity.Paciente;
import com.example.Agendamento_de_consulta.repository.PacienteRepository;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;

    // LISTA TODOS OS PACIENTES CADASTRADOS NO SISTEMA
    @Transactional(readOnly = true)
    public Iterable<Paciente> listarTodos() {
        return repository.findAll();
    }

    // BUSCA O PACIENTE PELO ID, CASO NÃO ENCONTRE, RETORNA O EXCEPTION HTTP 404
    @Transactional(readOnly = true)
    public Paciente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    // SALVAR / CADASTRAR O PACIENTE NO SISTEMA (POST)
    @Transactional
    public Paciente salvar(Paciente paciente) {

        
        // VALIDAÇÃO DE DUPLICIDADE, SE JÁ EXISTE UM PACIENTE COM ESSE CPF;
        if (repository.existsByCpf(paciente.getCpf())) {
            throw new BusinessException("Já existe um paciente cadastrado com este CPF.");
        }
        
        // VALIDAÇÃO DE DUPLICIDADE, SE JÁ EXISTE UM PACIENTE COM ESSE E-MAIL;
        if (repository.existsByEmailIgnoreCase(paciente.getEmail())) {
            throw new BusinessException("Já existe um paciente cadastrado com este E-mail.");
        }

        // VALIDAÇÃO DE DUPLICIDADE, SE JÁ EXISTE UM PACIENTE COM ESSE CARTÃO NACIONAL;
        if (paciente.getCartaoNacionalSaude() != null && !paciente.getCartaoNacionalSaude().isBlank()) {
            if (repository.existsByCartaoNacionalSaude(paciente.getCartaoNacionalSaude())) {
                throw new BusinessException("Já existe um paciente cadastrado com este Cartão Nacional de Saúde.");
            }
        }

        return repository.save(paciente);
    }

    // ATUALIZAR OS DADOS DE UM PACIENTE DO SISTEMA
    @Transactional
    public Paciente atualizar(Long id, Paciente dadosAtualizados) {
        Paciente pacienteAtual = buscarPorId(id);

    
        // VERIFICA SE O CPF PERTENCE A OUTRA PESSOA
        if (!pacienteAtual.getCpf().equals(dadosAtualizados.getCpf()) && 
            repository.existsByCpf(dadosAtualizados.getCpf())) {
            throw new BusinessException("O novo CPF informado já está sendo usado por outro paciente.");
        }

        // VERIFICA SE O E-MAIL PERTENCE A OUTRA PESSOA.
        if (!pacienteAtual.getEmail().equalsIgnoreCase(dadosAtualizados.getEmail()) && 
            repository.existsByEmailIgnoreCase(dadosAtualizados.getEmail())) {
            throw new BusinessException("O novo E-mail informado já está sendo usado por outro paciente.");
        }

        // ATUALIZANDO OS CAMPOS PERMITIDOS
        pacienteAtual.setNome(dadosAtualizados.getNome());
        pacienteAtual.setNomeSocial(dadosAtualizados.getNomeSocial());
        pacienteAtual.setCpf(dadosAtualizados.getCpf());
        pacienteAtual.setTelefone(dadosAtualizados.getTelefone());
        pacienteAtual.setEmail(dadosAtualizados.getEmail());
        pacienteAtual.setRg(dadosAtualizados.getRg());
        pacienteAtual.setOrgaoEmissor(dadosAtualizados.getOrgaoEmissor());
        pacienteAtual.setPeso(dadosAtualizados.getPeso());
        pacienteAtual.setAltura(dadosAtualizados.getAltura());
        pacienteAtual.setNomeMae(dadosAtualizados.getNomeMae());
        pacienteAtual.setCep(dadosAtualizados.getCep());
        pacienteAtual.setEndereco(dadosAtualizados.getEndereco());
        pacienteAtual.setComplemento(dadosAtualizados.getComplemento());
        pacienteAtual.setNumero(dadosAtualizados.getNumero());
        pacienteAtual.setBairro(dadosAtualizados.getBairro());
        pacienteAtual.setEstado(dadosAtualizados.getEstado());
        pacienteAtual.setCidade(dadosAtualizados.getCidade());
        pacienteAtual.setDataNascimento(dadosAtualizados.getDataNascimento());
        pacienteAtual.setSexo(dadosAtualizados.getSexo());
        pacienteAtual.setEstadoCivil(dadosAtualizados.getEstadoCivil());
        pacienteAtual.setCartaoNacionalSaude(dadosAtualizados.getCartaoNacionalSaude());

        return repository.save(pacienteAtual);
    }

    // DELETAR PACIENTE.(DELETE)
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        repository.deleteById(id);
    }
}