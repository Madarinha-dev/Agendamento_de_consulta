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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    // [ GET ] - LISTA TODOS OS AGENDAMENTOS CADASTRADOS (RETORNA 200)
    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    
    // [ GET ] - BUSCA AGENDAMENTO POR ID (RETORNA 200 OU 404)
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

   
    // [ GET ] - FILTRA E LISTA AGENDAMENTOS POR ID DO MÉDICO (RETORNA 200)
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(agendamentoService.listarPorMedico(medicoId));
    }


    // [ GET ] - FILTRA E LISTA AGENDAMENTOS POR ID POR PACIENTE (RETORNA 200)
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorPaciente(pacienteId));
    }

  
    // [ POST ] - REALIZA UM NOVO AGENDAMENTO DE CONSULTA COM VALIDAÇÃO (RETORNA 201)
    @PostMapping
    public ResponseEntity<AgendamentoResponse> agendar(@Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.agendar(request));
    }

    
    // [ PUT ] - ATUALIZA DADOS COMPLETOS DE UM AGENDAMENTO EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> alterar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.ok(agendamentoService.alterarAgendamento(id, request));
    }

    
    // [ PATCH ] - CANCELA UM AGENDAMENTO ESPECÍFICO MODIFICANDO SEU STATUS (RETORNA 204)
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
