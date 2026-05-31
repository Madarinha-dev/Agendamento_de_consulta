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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Agendamento_de_consulta.dto.MedicoRequest;
import com.example.Agendamento_de_consulta.dto.MedicoResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.service.MedicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Endpoints para gerenciamento de médicos")
public class MedicoController {

    private final MedicoService medicoService;

    // [ GET ] - LISTAR TODOS OS MÉDICOS CADASTRADOS
    @Operation(
        summary = "Lista todos os médicos",
        description = "Retorna uma lista completa com todos os médicos cadastrados no sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<MedicoResponse>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    
    // [ GET ] - BUSCA MÉDICO POR ID (RETORNA 200 OU 404)
    @Operation(
        summary = "Busca médico por ID",
        description = "Retorna os dados completos de um médico específico com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médico encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Médico não encontrado para o ID informado")
    })
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


    // [ GET ] - FILTRA MÉDICOS POR ID DA ESPECIALIDADE
    @Operation(
        summary = "Filtra médicos por especialidade",
        description = "Retorna uma lista de médicos vinculados a uma especialidade específica com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de médicos filtrada com sucesso")
    })
    @GetMapping("/especialidade")
    public ResponseEntity<List<MedicoResponse>> buscarPorEspecialidade(@RequestParam Long Blacklist_especialidadeId) {
        Especialidade Blacklist_especialidade = new Especialidade();
        Blacklist_especialidade.setId(Blacklist_especialidadeId);
        return ResponseEntity.ok(medicoService.buscarPorEspecialidade(Blacklist_especialidade));
    }

    
    // [ POST ] - CADASTRO DE NOVO MÉDICO COM VALIDAÇÃO (RETORNA 201)
     @Operation(
        summary = "Cadastra um novo médico",
        description = "Realiza o cadastro de um novo médico no sistema com validação dos dados informados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso")
    })
    @PostMapping
    public ResponseEntity<MedicoResponse> criar(@Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.salvar(request));
    }

    
    // [ PUT ] - ATUALIZA DADOS DE UM MÉDICO EXISTENTE (RETORNA 200)
   @Operation(
        summary = "Atualiza um médico existente",
        description = "Substitui completamente os dados de um médico existente com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequest dadosAtualizados) {
        return ResponseEntity.ok(medicoService.atualizar(id, dadosAtualizados));
    }

    
    // [ DELETE ] - EXCLUI MÉDICO DO SISTEMA (RETORNA 204)
      @Operation(
        summary = "Exclui um médico",
        description = "Remove permanentemente um médico do sistema com base no ID informado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Médico excluído com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
