
// package com.seuprojeto.entity;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String nomeSocial;

    @NotBlank(message = "CPF é obrigatório")
    @Column(unique = true)
    private String cpf;

    private String telefone;

    @Email(message = "Email inválido")
    private String email;

    private String rg;
    private String orgaoEmissor;
    private Double peso;
    private Double altura;
    private String nomeMae;
    private String cep;
    private String endereco;
    private String complemento;
    private String numero;
    private String bairro;
    private String estado;
    private String cidade;
    private LocalDate dataNascimento;
    private String sexo;
    private String estadoCivil;
    private String cartaoNacionalSaude;
}