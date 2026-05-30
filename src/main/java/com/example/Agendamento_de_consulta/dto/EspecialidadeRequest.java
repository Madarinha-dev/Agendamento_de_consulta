package com.example.Agendamento_de_consulta.dto;

import jakarta.validation.constraints.*;

public record EspecialidadeRequest (
    @NotBlank(message = "O nome da especialidade é obrigatório")
    @Size(max = 100, message = "O nome da especialidade não pode ter mais de 100 caracteres.")
    String nome,

    @NotBlank(message = "O código CBO é obrigatório")
    @Size(max = 20, message = "O código CBO não pode ter mais de 20 caracteres.")
    @Pattern(regexp = "^[0-9]{6}$", message = "O código CBO deve conter exatamente 6 números (ex: 225120).")
    String codigoCbo
) {}
