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

import com.example.Agendamento_de_consulta.dto.EspecialidadeRequest;
import com.example.Agendamento_de_consulta.dto.EspecialidadeResponse;
import com.example.Agendamento_de_consulta.service.EspecialidadeService;

@WebMvcTest(EspecialidadeController.class)
class EspecialidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EspecialidadeService especialidadeService;

    @Test
    @DisplayName("Cenário 1: Deve cadastrar uma nova especialidade com sucesso e retornar HTTP 201")
    void criarComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta para isolar dependências de construtores
        EspecialidadeResponse responseMock = mock(EspecialidadeResponse.class);

        // 2. Definimos o comportamento esperado do service
        when(especialidadeService.salvar(any(EspecialidadeRequest.class))).thenReturn(responseMock);

        // 3. Montamos o JSON cumprindo as regras de validação (Código CBO com 6 números)
        String jsonRequest = "{"
                + "\"nome\":\"Cardiologia\","
                + "\"codigoCbo\":\"225120\""
                + "}";

        // 4. Executamos o disparo HTTP POST para a rota
        mockMvc.perform(post("/api/especialidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cenário 2: Deve buscar especialidade por ID e retornar HTTP 200")
    void buscarPorIdComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta
        EspecialidadeResponse responseMock = mock(EspecialidadeResponse.class);

        // 2. Simulamos o service retornando essa resposta limpa
        when(especialidadeService.buscarPorId(eq(1L))).thenReturn(responseMock);

        // 3. Executamos o disparo HTTP GET para a rota por ID
        mockMvc.perform(get("/api/especialidades/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}