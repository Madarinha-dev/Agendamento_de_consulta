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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // [ GET ] - LISTA TODOS OS USUÁRIOS CADASTRADOS (RETORNA 200)
     @Operation(
        summary = "Lista todos os usuários",
        description = "Retorna uma lista completa com todos os usuários cadastrados no sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }


    // [ GET ] - BUSCA USUÁRIO POR ID (RETORNA 200 OU 404)
    @Operation(
        summary = "Busca usuário por ID",
        description = "Retorna os dados de um usuário específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }


    // [ POST ] - CADASTRO DE NOVO USUÁRIO COM VALIDAÇÃO (RETORNA 201)
    @Operation(
        summary = "Cadastra um novo usuário",
        description = "Realiza o cadastro de um novo usuário no sistema com validação dos dados informados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse salvo = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }


    // [ PUT ] - ATUALIZA DADOS DE UM USUÁRIO EXISTENTE (RETORNA 200)
    @Operation(
        summary = "Atualiza um usuário existente",
        description = "Substitui completamente os dados de um usuário existente com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest dadosAtualizados) {
        UsuarioResponse atualizado = usuarioService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(atualizado);
    }


    // [ DELETE ] - EXCLUI USUÁRIO DO SISTEMA (RETORNA 204)
    @Operation(
        summary = "Exclui um usuário",
        description = "Remove permanentemente um usuário do sistema com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}