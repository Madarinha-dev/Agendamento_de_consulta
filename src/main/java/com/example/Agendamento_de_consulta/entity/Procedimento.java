package com.example.Agendamento_de_consulta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "procedimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Procedimento {


    // COLUNAS SOBRE PROCEDIMENTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O tipo do procedimento é obrigatório")
    @Size(max = 50, message = "O tipo do produto não pode ter mais de 50 caracteres")
    @Column(name = "tipo_produto", nullable = false, length = 50)
    private String tipoProduto;

    @NotBlank(message = "O nome interno é obrigatório")
    @Size(max = 150, message = "O nome interno não pode ter mais de 150 caracteres")
    @Column(name = "nome_interno", nullable = false, length = 150)
    private String nomeInterno;



    @Size(max = 150, message = "O nome externo não pode ter mais de 150 caracteres")
    @Column(name = "nome_externo", length = 150)
    private String nomeExterno;

    @Size(max = 20, message = "O código TUSS não pode ter mais de 20 caracteres")
    @Column(name = "codigo_tuss", length = 20)
    private String codigoTuss;

    @Size(max = 20, message = "O código CBHPM não pode ter mais de 20 caracteres")
    @Column(name = "codigo_cbhpm", length = 20)
    private String codigoCbhpm;


    
    @Size(max = 20, message = "O código ambulatorial não pode ter mais de 20 caracteres")
    @Column(name = "codigo_ambulatorial", length = 20)
    private String codigoAmbulatorial;

    @Size(max = 500, message = "A observação não pode ter mais de 500 caracteres")
    @Column(length = 500)
    private String observacao;

    @NotBlank(message = "A duração da execução é obrigatória")
    @Size(max = 30, message = "A duração da execução não pode ter mais de 30 caracteres")
    @Column(name = "duracao_execucao", nullable = false, length = 30)
    private String duracaoExecucao;
}