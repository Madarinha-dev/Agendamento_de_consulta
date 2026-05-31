package com.example.Agendamento_de_consulta.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Agendamento_de_consulta.dto.AgendamentoRequest;
import com.example.Agendamento_de_consulta.dto.AgendamentoResponse;
import com.example.Agendamento_de_consulta.service.AgendamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Endpoints para gerenciamento de agendamentos de consultas médicas")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    // [ GET ] - LISTA TODOS OS AGENDAMENTOS CADASTRADOS (RETORNA 200)
    @Operation(
        summary = "Lista todos os agendamentos",
        description = "Retorna uma lista completa com todos os agendamentos cadastrados no sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    
    // [ GET ] - BUSCA AGENDAMENTO POR ID (RETORNA 200 OU 404)
    @Operation(
        summary = "Busca agendamento por ID",
        description = "Retorna os dados de um agendamento específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agendamento encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Agendamento não encontrado para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

   
    // [ GET ] - FILTRA E LISTA AGENDAMENTOS POR ID DO MÉDICO (RETORNA 200)
     @Operation(
        summary = "Lista agendamentos por médico",
        description = "Retorna todos os agendamentos vinculados a um médico específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de agendamentos do médico retornada com sucesso")
    })
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(agendamentoService.listarPorMedico(medicoId));
    }


    // [ GET ] - FILTRA E LISTA AGENDAMENTOS POR ID POR PACIENTE (RETORNA 200)
    @Operation(
        summary = "Lista agendamentos por paciente",
        description = "Retorna todos os agendamentos vinculados a um paciente específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de agendamentos do paciente retornada com sucesso")
    })

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorPaciente(pacienteId));
    }

  
    // [ POST ] - REALIZA UM NOVO AGENDAMENTO DE CONSULTA COM VALIDAÇÃO (RETORNA 201)
    @Operation(
        summary = "Cria um novo agendamento",
        description = "Realiza o agendamento de uma nova consulta médica com validação dos dados informados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<AgendamentoResponse> agendar(@Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.agendar(request));
    }

    
    // [ PUT ] - ATUALIZA DADOS COMPLETOS DE UM AGENDAMENTO EXISTENTE (RETORNA 200)
      @Operation(
        summary = "Atualiza um agendamento existente",
        description = "Substitui completamente os dados de um agendamento existente com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> alterar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.ok(agendamentoService.alterarAgendamento(id, request));
    }

    
    // [ PATCH ] - CANCELA UM AGENDAMENTO ESPECÍFICO MODIFICANDO SEU STATUS (RETORNA 204)
     @Operation(
        summary = "Cancela um agendamento",
        description = "Modifica o status de um agendamento específico para cancelado com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Agendamento cancelado com sucesso")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
