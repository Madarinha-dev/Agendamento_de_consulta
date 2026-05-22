package com.example.Agendamento_de_consulta.Controller;

import com.example.Agendamento_de_consulta.entity.Procedimento;
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
    public ResponseEntity<List<Procedimento>> listarTodos() {
        return ResponseEntity.ok(procedimentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procedimento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procedimentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Procedimento> criar(@Valid @RequestBody Procedimento procedimento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimentoService.salvar(procedimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Procedimento> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Procedimento dadosAtualizados) {
        return ResponseEntity.ok(procedimentoService.atualizar(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        procedimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
