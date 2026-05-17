package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    // COLUNAS SOBRE PACIENTE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nome;

    @Size(max = 150, message = "O nome social não pode ter mais de 150 caracteres")
    @Column(name = "nome_social", length = 150)
    private String nomeSocial;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "O telefone não pode ter mais de 20 caracteres")
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email não pode ter mais de 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20, message = "O RG não pode ter mais de 20 caracteres")
    @Column(length = 20)
    private String rg; 

    @Size(max = 20, message = "O órgão emisso não pode ter mais de 20 caracteres")
    @Column(name = "orgao_emissor", length = 20)
    private String orgaoEmissor;

    private Double peso;
    private Double altura;

    @Size(max = 150, message = "O nome da mãe não pode ter mais de 150 caracteres")
    @Column(name = "nome_mae", length = 150)
    private String nomeMae;

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 9, message = "O CEP deve ter entre 8 e 9 caracteres")
    @Column(nullable = false, length = 9)
    private String cep;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "O endereço não pode ter mais de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String endereco;

    @Size(max = 100, message = "O complemento não pode ter mais de 100 caracteres")
    @Column(length = 100)
    private String complemento;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "O número não pode ter mais de 10 caracteres")
    @Column(nullable = false, length = 10)
    private String numero;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "O bairro não pode ter mais de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String bairro;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve conter exatamente 2 caracteres")
    @Column(nullable = false, length = 2)
    private String estado;

    @NotBlank(message = "Cidade é obrigatório")
    @Size(max = 100, message = "A cidade não pode ter mais de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "Sexo é obrigatório")
    @Size(max = 20, message = "O sexo não pode ter mais de 20 caracteres")
    @Column(nullable = false, length = 20)
    private String sexo;

    @Size(max = 30, message = "O estado civil não pode ter mais de 30 caracteres")
    @Column(name = "estado_civil", length = 30)
    private String estadoCivil;

    @Size(max = 20, message = "O Cartão Nacional de Saúde não pode ter mais de 20 caracteres")
    @Column(name = "cartao_nacional_saude", length = 20)
    private String cartaoNacionalSaude;

}