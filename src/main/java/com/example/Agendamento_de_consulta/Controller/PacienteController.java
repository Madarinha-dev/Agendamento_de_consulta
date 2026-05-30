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

import com.example.Agendamento_de_consulta.dto.PacienteRequest;
import com.example.Agendamento_de_consulta.dto.PacienteResponse;
import com.example.Agendamento_de_consulta.service.PacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // [ GET ] - LISTA TODOS OS PACIENTES CADASTRADOS (RETORNA 200)
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }


    // [ GET ] - BUSCA PACIENTE POR ID (RETORNA 200 OU 404)
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }


    // [ POST ] - CADASTRO DE NOVO PACIENTE (RETORNA 201)
    @PostMapping
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.salvar(request));
    }


    // [ PUT ] - ATUALIZA DADOS DE UM PACIENTE EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest dadosAtualizados) {
        return ResponseEntity.ok(pacienteService.atualizar(id, dadosAtualizados));
    }


    // [ DELETE ] - EXCLUI PACIENTE DO SISTEMA (RETORNA 204)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}