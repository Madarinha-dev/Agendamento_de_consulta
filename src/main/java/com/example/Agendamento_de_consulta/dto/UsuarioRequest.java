package com.example.Agendamento_de_consulta.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
    String nome,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email não pode ter mais de 100 caracteres")
    String email,

    @Size(max = 100, message = "A profissão não pode ter mais de 100 caracteres")
    String profissao,

    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 14, message = "O CPF deve conter entre 11 e 14 caracteres")
    String cpf,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    String senha,

    @NotBlank(message = "A confirmação de senha é obrigatória")
    String confirmacaoSenha,

    @NotBlank(message = "As permissões de acesso são obrigatórias")
    @Size(max = 50, message = "As permissões de acesso não podem ter mais de 50 caracteres")
    String permissoesAcesso
) {}
