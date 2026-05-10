package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "procedimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O tipo do procedimento é obrigatório")
    private String tipoProduto;

    @NotBlank(message = "O nome interno é obrigatório")
    private String nomeInterno;

    private String nomeExterno;

    // Conforme sua modelagem: Codigos de faturamento
    private String codigoTuss;
    private String codigoCbhpm;
    private String codigoAmbulatorial;

    @Column(length = 500)
    private String observacao;

    private String duracaoExecucao;
}