package com.example.Agendamento_de_consulta.Controller;

import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<Medico>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping("/especialidade")
    public ResponseEntity<List<Medico>> buscarPorEspecialidade(@RequestParam Long especialidadeId) {
        Especialidade especialidade = new Especialidade();
        especialidade.setId(especialidadeId);
        return ResponseEntity.ok(medicoService.buscarPorEspecialidade(especialidade));
    }

    @PostMapping
    public ResponseEntity<Medico> criar(@Valid @RequestBody Medico medico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.salvar(medico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Medico dadosAtualizados) {
        return ResponseEntity.ok(medicoService.atualizar(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
