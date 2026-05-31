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

import com.example.Agendamento_de_consulta.dto.EspecialidadeRequest;
import com.example.Agendamento_de_consulta.dto.EspecialidadeResponse;
import com.example.Agendamento_de_consulta.service.EspecialidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "Endpoints para gerenciamento de especialidades médicas")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    // [ GET ] - LISTAR TODAS AS ESPECIALIDADES CADASTRADAS
     @Operation(
        summary = "Lista todas as especialidades",
        description = "Retorna uma lista completa com todas as especialidades médicas cadastradas no sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de especialidades retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<EspecialidadeResponse>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    
    // [ GET ] - BUSCAR ESPECIALIDADE POR ID (RETORNA 200 OU 404)
    @Operation(
        summary = "Busca especialidade por ID",
        description = "Retorna os dados de uma especialidade específica com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidade encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Especialidade não encontrada para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }


    // [ POST ] - CADASTRO DE NOVA ESPECIALIDADE COM VALIDAÇÃO (RETORNA 201)
    @Operation(
        summary = "Cadastra uma nova especialidade",
        description = "Realiza o cadastro de uma nova especialidade médica com validação dos dados informados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Especialidade cadastrada com sucesso")
    })
    @PostMapping
    public ResponseEntity<EspecialidadeResponse> criar(@Valid @RequestBody EspecialidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeService.salvar(request));
    }


    // [ PUT ] - ATUALIZA DADOS DE UMA ESPECIALIDADE EXISTENTE (RETORNA 200)
    @Operation(
        summary = "Atualiza uma especialidade existente",
        description = "Substitui completamente os dados de uma especialidade existente com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidade atualizada com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadeRequest dadosAtualizados) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, dadosAtualizados));
    }

    
    // [ DELETE ] - EXCLUI ESPECIALIDADE DO SISTEMA (RETORNA 204)
    @Operation(
        summary = "Exclui uma especialidade",
        description = "Remove permanentemente uma especialidade médica do sistema com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Especialidade excluída com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}