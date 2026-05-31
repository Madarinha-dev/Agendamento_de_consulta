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

import com.example.Agendamento_de_consulta.dto.PacienteRequest;
import com.example.Agendamento_de_consulta.dto.PacienteResponse;
import com.example.Agendamento_de_consulta.service.PacienteService;

@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Test
    @DisplayName("Cenário 1: Deve cadastrar um novo paciente com sucesso e retornar HTTP 201")
    void criarComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta isolando construtores do DTO
        PacienteResponse responseMock = mock(PacienteResponse.class);

        // 2. Definimos o comportamento esperado do service
        when(pacienteService.salvar(any(PacienteRequest.class))).thenReturn(responseMock);

        // 3. Montamos o JSON cumprindo todas as regras de validação (somente números em CPF/Telefone/CEP)
        String jsonRequest = "{"
                + "\"nome\":\"Randerson Medeiros\","
                + "\"cpf\":\"12345678901\","
                + "\"telefone\":\"84999999999\","
                + "\"email\":\"randerson.paciente@gmail.com\","
                + "\"cep\":\"59000000\","
                + "\"endereco\":\"Av. Principal\","
                + "\"numero\":\"123\","
                + "\"bairro\":\"Centro\","
                + "\"estado\":\"RN\","
                + "\"cidade\":\"Natal\","
                + "\"dataNascimento\":\"1995-10-20\","
                + "\"sexo\":\"Masculino\""
                + "}";

        // 4. Executamos o disparo HTTP POST para o endpoint
        mockMvc.perform(post("/api/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cenário 2: Deve buscar paciente por ID e retornar HTTP 200")
    void buscarPorIdComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta
        PacienteResponse responseMock = mock(PacienteResponse.class);

        // 2. Simulamos o service retornando essa resposta limpa
        when(pacienteService.buscarPorId(eq(1L))).thenReturn(responseMock);

        // 3. Executamos o disparo HTTP GET para a rota por ID
        mockMvc.perform(get("/api/pacientes/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}