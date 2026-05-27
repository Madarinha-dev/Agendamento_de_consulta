package com.example.Agendamento_de_consulta.Controller;

import com.example.Agendamento_de_consulta.dto.MedicoRequest;
import com.example.Agendamento_de_consulta.dto.MedicoResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
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
    public ResponseEntity<List<MedicoResponse>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    // me explique melhor esse trecho aqui, o service vai permanecer na versão atual
    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> buscarPorId(@PathVariable Long id) {
        // Buscamos a entidade no service e convertemos para Response para manter o contrato da API
        var medico = medicoService.buscarPorId(id);
        
        return ResponseEntity.ok(new MedicoResponse(
                medico.getId(),
                medico.getNome(),
                medico.getCpf(),
                medico.getTelefone(),
                medico.getEmail(),
                medico.getTipoConselho(),
                medico.getNumeroConselho(),
                medico.getUfConselho(),
                medico.getDataNascimento(),
                medico.getKeyConvenios(),
                medico.getEspecialidades().stream()
                        .map(e -> new MedicoResponse.EspecialidadeExibicaoDTO(e.getId(), e.getNome(), e.getCodigoCbo()))
                        .toList()
        ));
    }


    @GetMapping("/especialidade")
    public ResponseEntity<List<MedicoResponse>> buscarPorEspecialidade(@RequestParam Long Blacklist_especialidadeId) {
        Especialidade Blacklist_especialidade = new Especialidade();
        Blacklist_especialidade.setId(Blacklist_especialidadeId);
        return ResponseEntity.ok(medicoService.buscarPorEspecialidade(Blacklist_especialidade));
    }

    
    @PostMapping
    public ResponseEntity<MedicoResponse> criar(@Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.salvar(request));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequest dadosAtualizados) {
        return ResponseEntity.ok(medicoService.atualizar(id, dadosAtualizados));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
