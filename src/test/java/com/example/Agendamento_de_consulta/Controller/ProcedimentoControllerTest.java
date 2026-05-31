package com.example.Agendamento_de_consulta.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Agendamento_de_consulta.dto.ProcedimentoRequest;
import com.example.Agendamento_de_consulta.dto.ProcedimentoResponse;
import com.example.Agendamento_de_consulta.service.ProcedimentoService;

@WebMvcTest(ProcedimentoController.class)
class ProcedimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcedimentoService procedimentoService;

    @Test
    @DisplayName("Cenário 1: Deve cadastrar um novo procedimento com sucesso e retornar HTTP 201")
    void criarComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta isolando dependências de DTOs
        ProcedimentoResponse responseMock = mock(ProcedimentoResponse.class);

        // 2. Definimos o comportamento do service
        when(procedimentoService.salvar(any(ProcedimentoRequest.class))).thenReturn(responseMock);

        // 3. Montamos o JSON cumprindo as regras de validação obrigatórias da entidade
        String jsonRequest = "{"
                + "\"tipoProduto\":\"Exame\","
                + "\"nomeInterno\":\"Ecocardiograma Transtorácico\","
                + "\"nomeExterno\":\"Eco-Dopplercardiograma\","
                + "\"codigoTuss\":\"40101010\","
                + "\"duracaoExecucao\":\"30 minutos\""
                + "}";

        // 4. Executamos a chamada HTTP POST
        mockMvc.perform(post("/api/procedimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cenário 2: Deve buscar procedimento por ID e retornar HTTP 200")
    void buscarPorIdComSucesso() throws Exception {
        // 1. Criamos a resposta mockada limpa
        ProcedimentoResponse responseMock = mock(ProcedimentoResponse.class);

        // 2. Simulamos a devolução do service
        when(procedimentoService.buscarPorId(eq(1L))).thenReturn(responseMock);

        // 3. Executamos a chamada HTTP GET para a rota por ID
        mockMvc.perform(get("/api/procedimentos/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}