package com.example.Agendamento_de_consulta.Controller;

import com.example.Agendamento_de_consulta.entity.Agendamento;
import com.example.Agendamento_de_consulta.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    // [ GET ] - LISTA TODOS OS AGENDAMENTOS CADASTRADOS (RETORNA 200)
    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    
    // [ GET ] - BUSCA AGENDAMENTO POR ID (RETORNA 200 OU 404)
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

   
    // [ GET ] - FILTRA E LISTA AGENDAMENTOS POR ID DO MÉDICO (RETORNA 200)
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Agendamento>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(agendamentoService.listarPorMedico(medicoId));
    }


    // [ GET ] - FILTRA E LISTA AGENDAMENTOS POR ID POR PACIENTE (RETORNA 200)
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Agendamento>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorPaciente(pacienteId));
    }

  
    // [ POST ] - REALIZA UM NOVO AGENDAMENTO DE CONSULTA COM VALIDAÇÃO (RETORNA 201)
    @PostMapping
    public ResponseEntity<Agendamento> agendar(@Valid @RequestBody Agendamento agendamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.agendar(agendamento));
    }

    
    // [ PUT ] - ATUALIZA DADOS COMPLETOS DE UM AGENDAMENTO EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> alterar(
            @PathVariable Long id,
            @Valid @RequestBody Agendamento novosDados) {
        return ResponseEntity.ok(agendamentoService.alterarAgendamento(id, novosDados));
    }

    
    // [ PATCH ] - CANCELA UM AGENDAMENTO ESPECÍFICO MODIFICANDO SEU STATUS (RETORNA 204)
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
