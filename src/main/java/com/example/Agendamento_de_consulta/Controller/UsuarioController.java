package com.example.Agendamento_de_consulta.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Agendamento_de_consulta.dto.UsuarioRequest;
import com.example.Agendamento_de_consulta.dto.UsuarioResponse;
import com.example.Agendamento_de_consulta.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // [ GET ] - LISTA TODOS OS USUÁRIOS CADASTRADOS (RETORNA 200)
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }


    // [ GET ] - BUSCA USUÁRIO POR ID (RETORNA 200 OU 404)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }


    // [ POST ] - CADASTRO DE NOVO USUÁRIO COM VALIDAÇÃO (RETORNA 201)
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse salvo = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }


    // [ PUT ] - ATUALIZA DADOS DE UM USUÁRIO EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest dadosAtualizados) {
        UsuarioResponse atualizado = usuarioService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(atualizado);
    }


    // [ DELETE ] - EXCLUI USUÁRIO DO SISTEMA (RETORNA 204)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}