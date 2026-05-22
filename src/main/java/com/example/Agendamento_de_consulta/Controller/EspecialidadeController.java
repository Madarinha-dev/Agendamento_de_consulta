package com.example.Agendamento_de_consulta.Controller;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.service.EspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @GetMapping
    public ResponseEntity<List<Especialidade>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidade> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Especialidade> criar(@Valid @RequestBody Especialidade especialidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeService.salvar(especialidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Especialidade> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Especialidade dadosAtualizados) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}