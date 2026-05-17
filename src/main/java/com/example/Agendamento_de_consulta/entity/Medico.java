package com.example.Agendamento_de_consulta.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    // COLUNAS SOBRE MEDICO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nome;

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

    @NotBlank(message = "Tipo do conselho é obrigatório")
    @Size(max = 10, message = "O tipo do conselho não pode passar de 10 caracteres")
    @Column(name = "tipo_conselho", nullable = false, length = 10)
    private String tipoConselho;

    @NotBlank(message = "Número do conselho é obrigatório")
    @Size(max = 20, message = "O número do conselho não pode passar de 20 caracteres")
    @Column(name = "numero_conselho", nullable = false, length = 20)
    private String numeroConselho;

    @NotBlank(message = "UF do conselho é obrigatório")
    @Size(min = 2, max = 2, message = "A UF deve conter exatamente 2 caracteres")
    @Column(name = "uf_conselho", nullable = false, length = 2)
    private String ufConselho; //  UF_conselho

    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento; 

    @Column(name = "key_convenios")
    private String keyConvenios; 

    // RELACIONAMENTOS
    @ManyToMany
    @JoinTable(
        name = "medico_especialidade",
        joinColumns = @JoinColumn(name = "medico_id"),
        inverseJoinColumns = @JoinColumn(name = "especialidade_id")
    )
    private List<Especialidade> especialidades; 
    
}