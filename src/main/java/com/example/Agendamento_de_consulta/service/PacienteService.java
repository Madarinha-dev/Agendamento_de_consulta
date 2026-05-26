package com.example.Agendamento_de_consulta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.example.Agendamento_de_consulta.entity.Paciente;
import com.example.Agendamento_de_consulta.dto.PacienteRequest;
import com.example.Agendamento_de_consulta.dto.PacienteResponse;
import com.example.Agendamento_de_consulta.repository.PacienteRepository;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.exception.BusinessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;

    // LISTA TODOS OS PACIENTES CADASTRADOS NO SISTEMA
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        List<Paciente> pacientes = repository.findAll();
        return pacientes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // BUSCA O PACIENTE PELO ID, CASO NÃO ENCONTRE, RETORNA O EXCEPTION HTTP 404
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        return toResponse(paciente);
    }

    // SALVAR / CADASTRAR O PACIENTE NO SISTEMA (POST)
    @Transactional
    public PacienteResponse salvar(PacienteRequest request) {
        
        // VALIDAÇÃO DE DUPLICIDADE DE CPF
        if (repository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe um paciente cadastrado com este CPF.");
        }

        // VALIDAÇÃO DE DUPLICIDADE DE E-MAIL
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Já existe um paciente cadastrado com este E-mail.");
        }

        // VALIDAÇÃO DE DUPLICIDADE DO CARTÃO NACIONAL DE SAÚDE
        if (request.cartaoNacionalSaude() != null && !request.cartaoNacionalSaude().isBlank()) {
            if (repository.existsByCartaoNacionalSaude(request.cartaoNacionalSaude())) {
                throw new BusinessException("Já existe um paciente cadastrado com este Cartão Nacional de Saúde.");
            }
        }

        Paciente paciente = new Paciente();
        copiarDadosRequestParaEntidade(request, paciente);

        Paciente salvo = repository.save(paciente);
        return toResponse(salvo);
    }

    // ATUALIZAR OS DADOS DE UM PACIENTE DO SISTEMA
    @Transactional
    public PacienteResponse atualizar(Long id, PacienteRequest dadosAtualizados) {
        Paciente pacienteAtual = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        // VERIFICA SE O NOVO CPF PERTENCE A OUTRA PESSOA
        if (!pacienteAtual.getCpf().equals(dadosAtualizados.cpf()) && 
            repository.existsByCpf(dadosAtualizados.cpf())) {
            throw new BusinessException("O novo CPF informado já está sendo usado por outro paciente.");
        }

        // VERIFICA SE O NOVO E-MAIL PERTENCE A OUTRA PESSOA
        if (!pacienteAtual.getEmail().equalsIgnoreCase(dadosAtualizados.email()) && 
            repository.existsByEmailIgnoreCase(dadosAtualizados.email())) {
            throw new BusinessException("O novo E-mail informado já está sendo usado por outro paciente.");
        }

        copiarDadosRequestParaEntidade(dadosAtualizados, pacienteAtual);

        Paciente atualizado = repository.save(pacienteAtual);
        return toResponse(atualizado);
    }

    // DELETAR PACIENTE.(DELETE)
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        repository.deleteById(id);
    }

    private void copiarDadosRequestParaEntidade(PacienteRequest request, Paciente entidade) {
        entidade.setNome(request.nome());
        entidade.setNomeSocial(request.nomeSocial());
        entidade.setCpf(request.cpf());
        entidade.setTelefone(request.telefone());
        entidade.setEmail(request.email());
        entidade.setRg(request.rg());
        entidade.setOrgaoEmissor(request.orgaoEmissor());
        entidade.setPeso(request.peso());
        entidade.setAltura(request.altura());
        entidade.setNomeMae(request.nomeMae());
        entidade.setCep(request.cep());
        entidade.setEndereco(request.endereco());
        entidade.setComplemento(request.complemento());
        entidade.setNumero(request.numero());
        entidade.setBairro(request.bairro());
        entidade.setEstado(request.estado());
        entidade.setCidade(request.cidade());
        entidade.setDataNascimento(request.dataNascimento());
        entidade.setSexo(request.sexo());
        entidade.setEstadoCivil(request.estadoCivil());
        entidade.setCartaoNacionalSaude(request.cartaoNacionalSaude());
    }


    private PacienteResponse toResponse(Paciente paciente) {
        return new PacienteResponse(
            paciente.getId(),
            paciente.getNome(),
            paciente.getNomeSocial(),
            paciente.getCpf(),
            paciente.getTelefone(),
            paciente.getEmail(),
            paciente.getRg(),
            paciente.getOrgaoEmissor(),
            paciente.getPeso(),
            paciente.getAltura(),
            paciente.getNomeMae(),
            paciente.getCep(),
            paciente.getEndereco(),
            paciente.getComplemento(),
            paciente.getNumero(),
            paciente.getBairro(),
            paciente.getEstado(),
            paciente.getCidade(),
            paciente.getDataNascimento(),
            paciente.getSexo(),
            paciente.getEstadoCivil(),
            paciente.getCartaoNacionalSaude()
        );
    }
}