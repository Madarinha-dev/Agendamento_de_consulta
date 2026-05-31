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

import com.example.Agendamento_de_consulta.dto.PacienteRequest;
import com.example.Agendamento_de_consulta.dto.PacienteResponse;
import com.example.Agendamento_de_consulta.service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Endpoints para gerenciamento de pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    // [ GET ] - LISTA TODOS OS PACIENTES CADASTRADOS (RETORNA 200)
    @Operation(
        summary = "Lista todos os pacientes",
        description = "Retorna uma lista completa com todos os pacientes cadastrados no sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }


    // [ GET ] - BUSCA PACIENTE POR ID (RETORNA 200 OU 404)
    @Operation(
        summary = "Busca paciente por ID",
        description = "Retorna os dados de um paciente específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }


    // [ POST ] - CADASTRO DE NOVO PACIENTE (RETORNA 201)
    @Operation(
        summary = "Cadastra um novo paciente",
        description = "Realiza o cadastro de um novo paciente no sistema com validação dos dados informados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso")
    })
    @PostMapping
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.salvar(request));
    }


    // [ PUT ] - ATUALIZA DADOS DE UM PACIENTE EXISTENTE (RETORNA 200)
    @Operation(
        summary = "Atualiza um paciente existente",
        description = "Substitui completamente os dados de um paciente existente com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest dadosAtualizados) {
        return ResponseEntity.ok(pacienteService.atualizar(id, dadosAtualizados));
    }


    // [ DELETE ] - EXCLUI PACIENTE DO SISTEMA (RETORNA 204)
    @Operation(
        summary = "Exclui um paciente",
        description = "Remove permanentemente um paciente do sistema com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}