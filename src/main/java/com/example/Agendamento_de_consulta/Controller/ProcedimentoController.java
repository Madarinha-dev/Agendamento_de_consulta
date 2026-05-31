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

import com.example.Agendamento_de_consulta.dto.ProcedimentoRequest;
import com.example.Agendamento_de_consulta.dto.ProcedimentoResponse;
import com.example.Agendamento_de_consulta.service.ProcedimentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
@Tag(name = "Procedimentos", description = "Endpoints para gerenciamento de procedimentos médicos")
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    // [ GET ] - LISTA TODOS OS PROCEDIMENTOS CADASTRADOS (RETORNA 200)
    @Operation(
        summary = "Lista todos os procedimentos",
        description = "Retorna uma lista completa com todos os procedimentos médicos cadastrados no sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de procedimentos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProcedimentoResponse>> listarTodos() {
        return ResponseEntity.ok(procedimentoService.listarTodos());
    }


    // [ GET ] - BUSCA PROCEDIMENTO POR ID (RETORNA 200 OU 404)
    @Operation(
        summary = "Busca procedimento por ID",
        description = "Retorna os dados de um procedimento específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Procedimento encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Procedimento não encontrado para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procedimentoService.buscarPorId(id));
    }


    // [ POST ] - CADASTRO DE NOVO PROCEDIMENTO COM VALIDAÇÃO (RETORNA 201)
     @Operation(
        summary = "Cadastra um novo procedimento",
        description = "Realiza o cadastro de um novo procedimento médico no sistema com validação dos dados informados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Procedimento cadastrado com sucesso")
    })
    @PostMapping
    public ResponseEntity<ProcedimentoResponse> criar(@Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimentoService.salvar(request));
    }

    
    // [ PUT ] - ATUALIZA DADOS DE UM PROCEDIMENTO EXISTENTE (RETORNA 200)
    @Operation(
        summary = "Atualiza um procedimento existente",
        description = "Substitui completamente os dados de um procedimento existente com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Procedimento atualizado com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.ok(procedimentoService.atualizar(id, request));
    }


    // [ DELETE ] - EXCLUI PROCEDIMENTO DO SISTEMA (RETORNA 204)
     @Operation(
        summary = "Exclui um procedimento",
        description = "Remove permanentemente um procedimento médico do sistema com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Procedimento excluído com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        procedimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
