package com.example.Agendamento_de_consulta.Controller;

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

import com.example.Agendamento_de_consulta.dto.ProcedimentoRequest;
import com.example.Agendamento_de_consulta.dto.ProcedimentoResponse;
import com.example.Agendamento_de_consulta.service.ProcedimentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    // [ GET ] - LISTA TODOS OS PROCEDIMENTOS CADASTRADOS (RETORNA 200)
    @GetMapping
    public ResponseEntity<List<ProcedimentoResponse>> listarTodos() {
        return ResponseEntity.ok(procedimentoService.listarTodos());
    }


    // [ GET ] - BUSCA PROCEDIMENTO POR ID (RETORNA 200 OU 404)
    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procedimentoService.buscarPorId(id));
    }


    // [ POST ] - CADASTRO DE NOVO PROCEDIMENTO COM VALIDAÇÃO (RETORNA 201)
    @PostMapping
    public ResponseEntity<ProcedimentoResponse> criar(@Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimentoService.salvar(request));
    }

    
    // [ PUT ] - ATUALIZA DADOS DE UM PROCEDIMENTO EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.ok(procedimentoService.atualizar(id, request));
    }


    // [ DELETE ] - EXCLUI PROCEDIMENTO DO SISTEMA (RETORNA 204)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        procedimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
