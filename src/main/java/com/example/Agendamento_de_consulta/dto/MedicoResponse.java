package com.example.Agendamento_de_consulta.dto;

import java.time.LocalDate;
import java.util.List;

public record MedicoResponse(
    Long id,
    String nome,
    String cpf,
    String telefone,
    String email,
    String tipoConselho,
    String numeroConselho,
    String ufConselho,
    LocalDate dataNascimento,
    String keyConvenios,
    List<EspecialidadeExibicaoDTO> especialidades
) {
    // ESSE SUB RECORD, ELE EXISTE DEVIDO A RELAÇÃO DE CHAVE ESTRANGEIRA
    // COM A TABELA ESPECIALIDADE, SÓ PARA RETORNAR OS DADOS AO CLIENTE,
    // N EXPOR A ENTIDADE;
    public record EspecialidadeExibicaoDTO(
        Long id,
        String nome,
        String codigoCbo
    ) {}
}
