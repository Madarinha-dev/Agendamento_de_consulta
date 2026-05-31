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

import com.example.Agendamento_de_consulta.dto.AgendamentoRequest;
import com.example.Agendamento_de_consulta.dto.AgendamentoResponse;
import com.example.Agendamento_de_consulta.service.AgendamentoService;

@WebMvcTest(AgendamentoController.class)
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamentoService agendamentoService;

    @Test
    @DisplayName("Cenário 1: Deve realizar um novo agendamento com sucesso e retornar HTTP 201")
    void agendarComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta
        AgendamentoResponse responseMock = mock(AgendamentoResponse.class);

        // 2. Definimos o comportamento do service
        when(agendamentoService.agendar(any(AgendamentoRequest.class))).thenReturn(responseMock);

        // 3. String JSON incluindo os campos obrigatórios exigidos pelas validações da DTO
        String jsonRequest = "{"
                + "\"medicoId\":1,"
                + "\"pacienteId\":1,"
                + "\"dataHoraAgendamento\":\"2030-12-31T10:00:00\","
                + "\"statusAgendamento\":\"AGENDADO\","
                + "\"dataHoraAgendaMedico\":\"2030-12-31T10:00:00\""
                + "}";

        // 4. Executa o disparo HTTP POST para a rota
        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cenário 2: Deve buscar agendamento por ID e retornar HTTP 200")
    void buscarPorIdComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta
        AgendamentoResponse responseMock = mock(AgendamentoResponse.class);

        // 2. Simulamos o service retornando essa resposta limpa
        when(agendamentoService.buscarPorId(eq(1L))).thenReturn(responseMock);

        // 3. Executamos o disparo HTTP GET para a rota por ID
        mockMvc.perform(get("/api/agendamentos/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}