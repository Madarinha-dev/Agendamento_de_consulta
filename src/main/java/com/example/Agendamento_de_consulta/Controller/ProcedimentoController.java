package com.example.Agendamento_de_consulta.Controller;

import com.example.Agendamento_de_consulta.dto.ProcedimentoRequest;
import com.example.Agendamento_de_consulta.dto.ProcedimentoResponse;
import com.example.Agendamento_de_consulta.service.ProcedimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    @GetMapping
    public ResponseEntity<List<ProcedimentoResponse>> listarTodos() {
        return ResponseEntity.ok(procedimentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procedimentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProcedimentoResponse> criar(@Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimentoService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.ok(procedimentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        procedimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
