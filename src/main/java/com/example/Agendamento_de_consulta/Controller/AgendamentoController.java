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

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

   
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Agendamento>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(agendamentoService.listarPorMedico(medicoId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Agendamento>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorPaciente(pacienteId));
    }

  
    @PostMapping
    public ResponseEntity<Agendamento> agendar(@Valid @RequestBody Agendamento agendamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.agendar(agendamento));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> alterar(
            @PathVariable Long id,
            @Valid @RequestBody Agendamento novosDados) {
        return ResponseEntity.ok(agendamentoService.alterarAgendamento(id, novosDados));
    }

    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
