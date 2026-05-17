package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    // COLUNAS SOBRE USUARIO;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email não pode ter mais de 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String email;



    @Size(max = 100, message = "A profissão não pode ter mais de 100 caracteres")
    @Column(length = 100)
    private String profissao;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 14, message = "O CPF deve conter entre 11 e 14 caracteres")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    @Column(nullable = false)
    private String senha;


    // Anotação para n persistir no banco de dados, não criar a coluna, para ser ignorado no mapeamento;
    @Transient 
    private String confirmacaoSenha;

    @NotBlank(message = "As permissões de acesso são obrigatórias")
    @Size(max = 50, message = "As permissões de acesso não podem ter mais de 50 caracteres")
    @Column(name = "permissoes_acesso", nullable = false, length = 50)
    private String permissoesAcesso;

}