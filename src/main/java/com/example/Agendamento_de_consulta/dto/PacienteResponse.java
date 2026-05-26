package com.example.Agendamento_de_consulta.dto;
import java.time.LocalDate;


public record PacienteResponse (
    Long id,
    String nome,
    String nomeSocial,
    String cpf,
    String telefone,
    String email,
    String rg,
    String orgaoEmissor,
    Double peso,
    Double altura,
    String nomeMae,
    String cep,
    String endereco,
    String complemento,
    String numero,
    String bairro,
    String estado,
    String cidade,
    LocalDate dataNascimento,
    String sexo,
    String estadoCivil,
    String cartaoNacionalSaude
) {}
