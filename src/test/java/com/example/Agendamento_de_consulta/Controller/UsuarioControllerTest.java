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

import com.example.Agendamento_de_consulta.dto.UsuarioRequest;
import com.example.Agendamento_de_consulta.dto.UsuarioResponse;
import com.example.Agendamento_de_consulta.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Cenário 1: Deve cadastrar um novo usuário com sucesso e retornar HTTP 201")
    void criarComSucesso() throws Exception {
        // 1. Criamos a instância mockada da resposta isolando dependências de DTOs
        UsuarioResponse responseMock = mock(UsuarioResponse.class);

        // 2. Definimos o comportamento do service
        when(usuarioService.salvar(any(UsuarioRequest.class))).thenReturn(responseMock);

        // 3. Montamos o JSON cumprindo todas as regras de validação obrigatórias (Senha >= 6 caracteres e Email válido)
        String jsonRequest = "{"
                + "\"nome\":\"Randerson Medeiros\","
                + "\"email\":\"randerson.usuario@sistema.com\","
                + "\"profissao\":\"Administrador\","
                + "\"cpf\":\"12345678901\","
                + "\"senha\":\"senha123\","
                + "\"confirmacaoSenha\":\"senha123\","
                + "\"permissoesAcesso\":\"ADMIN\""
                + "}";

        // 4. Executamos a chamada HTTP POST
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cenário 2: Deve buscar usuário por ID e retornar HTTP 200")
    void buscarPorIdComSucesso() throws Exception {
        // 1. Criamos a resposta mockada limpa
        UsuarioResponse responseMock = mock(UsuarioResponse.class);

        // 2. Simulamos a devolução do service
        when(usuarioService.buscarPorId(eq(1L))).thenReturn(responseMock);

        // 3. Executamos a chamada HTTP GET para a rota por ID
        mockMvc.perform(get("/api/usuarios/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}