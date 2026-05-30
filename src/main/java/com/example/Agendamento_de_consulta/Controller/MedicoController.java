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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Agendamento_de_consulta.dto.MedicoRequest;
import com.example.Agendamento_de_consulta.dto.MedicoResponse;
import com.example.Agendamento_de_consulta.entity.Especialidade;
import com.example.Agendamento_de_consulta.service.MedicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    // [ GET ] - LISTAR TODOS OS MÉDICOS CADASTRADOS
    @GetMapping
    public ResponseEntity<List<MedicoResponse>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    
    // [ GET ] - BUSCA MÉDICO POR ID (RETORNA 200 OU 404)
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
    @GetMapping("/especialidade")
    public ResponseEntity<List<MedicoResponse>> buscarPorEspecialidade(@RequestParam Long Blacklist_especialidadeId) {
        Especialidade Blacklist_especialidade = new Especialidade();
        Blacklist_especialidade.setId(Blacklist_especialidadeId);
        return ResponseEntity.ok(medicoService.buscarPorEspecialidade(Blacklist_especialidade));
    }

    
    // [ POST ] - CADASTRO DE NOVO MÉDICO COM VALIDAÇÃO (RETORNA 201)
    @PostMapping
    public ResponseEntity<MedicoResponse> criar(@Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.salvar(request));
    }

    
    // [ PUT ] - ATUALIZA DADOS DE UM MÉDICO EXISTENTE (RETORNA 200)
    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequest dadosAtualizados) {
        return ResponseEntity.ok(medicoService.atualizar(id, dadosAtualizados));
    }

    
    // [ DELETE ] - EXCLUI MÉDICO DO SISTEMA (RETORNA 204)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
