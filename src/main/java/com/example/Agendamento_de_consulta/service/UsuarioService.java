package com.example.Agendamento_de_consulta.service;

import com.example.Agendamento_de_consulta.entity.Usuario;
import com.example.Agendamento_de_consulta.exception.BusinessException;
import com.example.Agendamento_de_consulta.exception.ResourceNotFoundException;
import com.example.Agendamento_de_consulta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    // LISTA TODOS OS USUÁRIOS CADASTRADOS; RNF04
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    // BUSCAR USUÁRIO PELO ID
    // CASO NÃO ENCONTRE, RETORNA EXCEPTION HTTP 404 (RNF05).
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    // SALVA UM NOVO USUÁRIO COM VALIDAÇÕES E REGRA DE NEGÓCIO.
    // RNF02 E RNF04, evitar duplicidade de CPF e EMAIL.
    @Transactional
    public Usuario salvar(Usuario usuario) {
        // VALIDAÇÃO 01: Verificar se as senhas batem
        if (!usuario.getSenha().equals(usuario.getConfirmacaoSenha())) {
            throw new BusinessException("A senha e a confirmação de senha não coincidem.");
        }

        // VALIDAÇÃO 02: Verifica se o CPF, já existe no sistema
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new BusinessException("Já existe um usuário cadastrado com estre CPF.");
        }

        // VALIDAÇÃO 03: verifica se o E-MAIL já existe.
        if (usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com este E-mail.");
        }

        return usuarioRepository.save(usuario);
    }


    // ATUALIZA OS DADOS DE UM USUÁRIO EXISTENTE.
    @Transactional
    public Usuario atualizar(Long id, Usuario dadosAtualizados) {
        Usuario usuarioAtual = buscarPorId(id);

        // VALIDAÇÃO DE DUPLICIDADE SOBRE CPF
        if (!usuarioAtual.getCpf().equals(dadosAtualizados.getCpf()) && 
            usuarioRepository.existsByCpf(dadosAtualizados.getCpf())) {
            throw new BusinessException("O novo CPF informado já está em uso por outro usuário.");
        }


        // VALIDAÇÃO DE DUPLICIDADE SOBRE EMAIL
        if (!usuarioAtual.getEmail().equalsIgnoreCase(dadosAtualizados.getEmail()) && 
            usuarioRepository.existsByEmailIgnoreCase(dadosAtualizados.getEmail())) {
            throw new BusinessException("O novo E-mail informado já está em uso por outro usuário.");
        }

        // ATUALIZAÇÃO DOS CAMPOS, APÓS AS VALIDAÇÕES
        usuarioAtual.setNome(dadosAtualizados.getNome());
        usuarioAtual.setEmail(dadosAtualizados.getEmail());
        usuarioAtual.setProfissao(dadosAtualizados.getProfissao());
        usuarioAtual.setCpf(dadosAtualizados.getCpf());

        // SE O FRONT-END ENVIAR UMA NOVA SENHA, VALIDA E ALTERA
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isBlank()) {
            if (!dadosAtualizados.getSenha().equals(dadosAtualizados.getConfirmacaoSenha())) {
                throw new BusinessException("A nova senha e a confirmação não coincidem.");
            }
            usuarioAtual.setSenha(dadosAtualizados.getSenha());
            usuarioAtual.setConfirmacaoSenha(dadosAtualizados.getConfirmacaoSenha());
        }

        usuarioAtual.setPermissoesAcesso(dadosAtualizados.getPermissoesAcesso());
        return usuarioRepository.save(usuarioAtual);
    }

    // EXCLUI UM USUÁRIO DO SISTEMA.
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário",id);
        }
        usuarioRepository.deleteById(id);
    }
}
