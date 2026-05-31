package com.example.Agendamento_de_consulta.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Agendamento_de_consulta.dto.MedicoRequest;
import com.example.Agendamento_de_consulta.dto.MedicoResponse;
import com.example.Agendamento_de_consulta.entity.Medico;
import com.example.Agendamento_de_consulta.service.MedicoService;

@WebMvcTest(MedicoController.class)
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoService medicoService;

    @Test
    @DisplayName("Cenário 1: Deve cadastrar um novo médico com sucesso e retornar HTTP 201")
    void criarComSucesso() throws Exception {
        // 1. Instanciamos a resposta DTO simulada pelo Mockito para o Service
        MedicoResponse responseMock = mock(MedicoResponse.class);
        when(medicoService.salvar(any(MedicoRequest.class))).thenReturn(responseMock);

        // 2. Criamos o JSON simulando a requisição com todas as regras exigidas pelas validações
        String jsonRequest = "{"
                + "\"nome\":\"Dr. Randerson Medeiros\","
                + "\"cpf\":\"12345678901\","
                + "\"telefone\":\"84999999999\","
                + "\"email\":\"randerson.medico@clinica.com\","
                + "\"tipoConselho\":\"CRM\","
                + "\"numeroConselho\":\"123456\","
                + "\"ufConselho\":\"RN\","
                + "\"dataNascimento\":\"1990-05-15\","
                + "\"especialidadesIds\":[1]"
                + "}";

        // 3. Executamos a requisição POST
        mockMvc.perform(post("/api/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cenário 2: Deve buscar médico por ID e retornar HTTP 200")
    void buscarPorIdComSucesso() throws Exception {
        // 1. Como seu controller chama .getEspecialidades().stream() na entidade Medico,
        // precisamos criar um objeto Medico real ou mockado bem estruturado para o service retornar.
        Medico medicoMock = new Medico();
        medicoMock.setId(1L);
        medicoMock.setNome("Dr. Randerson Medeiros");
        medicoMock.setCpf("12345678901");
        medicoMock.setTelefone("84999999999");
        medicoMock.setEmail("randerson.medico@clinica.com");
        medicoMock.setTipoConselho("CRM");
        medicoMock.setNumeroConselho("123456");
        medicoMock.setUfConselho("RN");
        medicoMock.setDataNascimento(LocalDate.of(1990, 5, 15));
        medicoMock.setEspecialidades(new ArrayList<>()); // Evita NullPointerException no stream do controller

        // 2. Definimos o comportamento do mock do service retornando a entidade física
        when(medicoService.buscarPorId(eq(1L))).thenReturn(medicoMock);

        // 3. Executamos a requisição GET por ID
        mockMvc.perform(get("/api/medicos/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}