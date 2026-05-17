package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "especialidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidade {
    

    // Colunas SOBRE A ESPECIALIDADE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da especialidade é obrigatório")
    @Size(max = 100, message = "o nome da especialidade não pode ter mais de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O código CBO é obrigatório")
    @Size(max = 20, message = "O código CBO não pode ter mais de 20 caracteres.")
    @Column(name = "codigo_cbo", nullable = false, length = 20)
    private String codigoCbo;
}
