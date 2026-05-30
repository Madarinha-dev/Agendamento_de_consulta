package com.example.Agendamento_de_consulta.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PacienteRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
    String nome,


    @Size(max = 150, message = "O nome social não pode ter mais de 150 caracteres")
    String nomeSocial,


    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "O CPF deve conter apenas números")
    String cpf,


    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "O telefone não pode ter mais de 20 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "O telefone deve conter apenas números")
    String telefone,


    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email não pode ter mais de 100 caracteres")
    String email,


    @Size(max = 20, message = "O RG não pode ter mais de 20 caracteres")
    String rg,


    @Size(max = 20, message = "O órgão emissor não pode ter mais de 20 caracteres")
    String orgaoEmissor,
    Double peso,
    Double altura,


    @Size(max = 150, message = "O nome da mãe não pode ter mais de 150 caracteres")
    String nomeMae,


    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 9, message = "O CEP deve ter entre 8 e 9 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "O CEP deve conter apenas números")
    String cep,


    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "O endereço não pode ter mais de 200 caracteres")
    String endereco,


    @Size(max = 100, message = "O complemento não pode ter mais de 100 caracteres")
    String complemento,


    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "O número não pode ter mais de 10 caracteres")
    String numero,


    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "O bairro não pode ter mais de 100 caracteres")
    String bairro,


    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve conter exatamente 2 caracteres")
    String estado,


    @NotBlank(message = "Cidade é obrigatório")
    @Size(max = 100, message = "A cidade não pode ter mais de 100 caracteres")
    String cidade,


    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser uma data passada")
    LocalDate dataNascimento,


    @NotBlank(message = "Sexo é obrigatório")
    @Size(max = 20, message = "O sexo não pode ter mais de 20 caracteres")
    String sexo,


    @Size(max = 30, message = "O estado civil não pode ter mais de 30 caracteres")
    String estadoCivil,


    @Size(max = 20, message = "O Cartão Nacional de Saúde não pode ter mais de 20 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O Cartão Nacional de Saúde deve conter apenas números")
    String cartaoNacionalSaude
) {}
