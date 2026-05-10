package com.example.Agendamento_de_consulta.entity;
// package com.seuprojeto.entity;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.*;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;
// import java.time.LocalDate;
// import java.util.List;

// @Entity
// @Table(name = "medicos")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class Medico {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @NotBlank(message = "Nome é obrigatório")
//     private String nome;

//     @NotBlank(message = "CPF é obrigatório")
//     @Column(unique = true)
//     private String cpf;

//     private String telefone;

//     @Email(message = "Email inválido")
//     private String email;

//     private String tipoConselho; // Ex: CRM, CRO
//     private String numeroConselho;
//     private String ufConselho;
//     private LocalDate dataNascimento;

//     // Relacionamento Many-to-Many com Especialidade (RF13)
//     @ManyToMany
//     @JoinTable(
//         name = "medico_especialidade",
//         joinColumns = @JoinColumn(name = "medico_id"),
//         inverseJoinColumns = @JoinColumn(name = "especialidade_id")
//     )
//     private List<Especialidade> especialidades;
// }