package com.example.Agendamento_de_consulta.controller;

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

import com.example.Agendamento_de_consulta.dto.EspecialidadeRequest;
import com.example.Agendamento_de_consulta.dto.EspecialidadeResponse;
import com.example.Agendamento_de_consulta.service.EspecialidadeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    // [ GET ] - LISTAR TODAS AS ESPECIALIDADES CADASTRADAS
    @GetMapping
    public ResponseEntity<List<EspecialidadeResponse>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    
    // [ GET ] - BUSCAR ESPECIALIDADE POR ID (RETORNA 200 OU 404)
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }


    // [ POST ] - CADASTRO DE NOVA ESPECIALIDADE COM VALIDAÇÃO (RETORNA 201)
    @PostMapping
    public ResponseEntity<EspecialidadeResponse> criar(@Valid @RequestBody EspecialidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeService.salvar(request));
    }


    // [ PUT ] - ATUALIZA DADOS DE UMA ESPECIALIDADE EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadeRequest dadosAtualizados) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, dadosAtualizados));
    }

    
    // [ DELETE ] - EXCLUI ESPECIALIDADE DO SISTEMA (RETORNA 204)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}