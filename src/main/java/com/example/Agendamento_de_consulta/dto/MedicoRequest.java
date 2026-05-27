package com.example.Agendamento_de_consulta.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record MedicoRequest (
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
    String nome,

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    String cpf,

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "O telefone não pode ter mais de 20 caracteres")
    String telefone,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email não pode ter mais de 100 caracteres")
    String email,

    @NotBlank(message = "Tipo do conselho é obrigatório")
    @Size(max = 10, message = "O tipo do conselho não pode passar de 10 caracteres")
    String tipoConselho,

    @NotBlank(message = "Número do conselho é obrigatório")
    @Size(max = 20, message = "O número do conselho não pode passar de 20 caracteres")
    String numeroConselho,

    @NotBlank(message = "UF do conselho é obrigatório")
    @Size(min = 2, max = 2, message = "A UF deve conter exatamente 2 caracteres")
    String ufConselho,

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser uma data passada.")
    LocalDate dataNascimento,

    String keyConvenios,

    @NotEmpty(message = "O médico deve possuir pelo menos uma especialidade")
    List<Long> especialidadesIds
) {}
