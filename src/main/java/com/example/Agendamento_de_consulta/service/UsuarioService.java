package com.example.Agendamento_de_consulta.service;

import com.example.Agendamento_de_consulta.dto.UsuarioRequest;
import com.example.Agendamento_de_consulta.dto.UsuarioResponse;
import com.example.Agendamento_de_consulta.entity.Usuario;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    // LISTA TODOS OS USUÁRIOS CADASTRADOS; RNF04
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        return toResponse(usuario);
    }


    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request) {
        // VALIDAÇÃO 01: Verificar se as senhas batem
        if (!request.senha().equals(request.confirmacaoSenha())) {
            throw new BusinessException("A senha e a confirmação de senha não coincidem.");
        }

        // VALIDAÇÃO 02: Verifica se o CPF já existe no sistema
        if (usuarioRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe um usuário cadastrado com este CPF.");
        }

        // VALIDAÇÃO 03: Verifica se o E-MAIL já existe
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Já existe um usuário cadastrado com este E-mail.");
        }

        // Converte o DTO Request para a Entidade antes de salvar
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setProfissao(request.profissao());
        usuario.setCpf(request.cpf());
        usuario.setSenha(request.senha());
        usuario.setConfirmacaoSenha(request.confirmacaoSenha());
        usuario.setPermissoesAcesso(request.permissoesAcesso());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        return toResponse(usuarioSalvo);
    }


    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest dadosAtualizados) {
        // Busca a entidade pura do banco para manipulação
        Usuario usuarioAtual = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        // VALIDAÇÃO DE DUPLICIDADE SOBRE CPF
        if (!usuarioAtual.getCpf().equals(dadosAtualizados.cpf()) && 
            usuarioRepository.existsByCpf(dadosAtualizados.cpf())) {
            throw new BusinessException("O novo CPF informado já está em uso por outro usuário.");
        }

        // VALIDAÇÃO DE DUPLICIDADE SOBRE EMAIL
        if (!usuarioAtual.getEmail().equalsIgnoreCase(dadosAtualizados.email()) && 
            usuarioRepository.existsByEmailIgnoreCase(dadosAtualizados.email())) {
            throw new BusinessException("O novo E-mail informado já está em uso por outro usuário.");
        }

        // ATUALIZAÇÃO DOS CAMPOS VINDO DO RECORD REQUEST
        usuarioAtual.setNome(dadosAtualizados.nome());
        usuarioAtual.setEmail(dadosAtualizados.email());
        usuarioAtual.setProfissao(dadosAtualizados.profissao());
        usuarioAtual.setCpf(dadosAtualizados.cpf());

        // SE O FRONT-END ENVIAR UMA NOVA SENHA, VALIDA E ALTERA
        if (dadosAtualizados.senha() != null && !dadosAtualizados.senha().isBlank()) {
            if (!dadosAtualizados.senha().equals(dadosAtualizados.confirmacaoSenha())) {
                throw new BusinessException("A nova senha e a confirmação não coincidem.");
            }
            usuarioAtual.setSenha(dadosAtualizados.senha());
            usuarioAtual.setConfirmacaoSenha(dadosAtualizados.confirmacaoSenha());
        }

        usuarioAtual.setPermissoesAcesso(dadosAtualizados.permissoesAcesso());
        
        Usuario usuarioSalvo = usuarioRepository.save(usuarioAtual);
        return toResponse(usuarioSalvo);
    }

    
    // EXCLUI UM USUÁRIO DO SISTEMA.
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário",id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getProfissao(),
            usuario.getCpf(),
            usuario.getPermissoesAcesso()
        );
    }
}
