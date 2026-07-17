package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private Cliente criarCliente(Long id) {
        Cliente cliente = new Cliente();
        cliente.setCodigoCliente(id);
        cliente.setNomeCliente("Fulano de Tal");
        cliente.setEnderecoCliente("Rua A, 123");
        cliente.setTelefoneCliente("11999999999");
        return cliente;
    }

    @Test
    void cadastrarCliente_dadosValidos_retorna201ComClienteCriado() throws Exception {
        when(clienteService.salvarCliente(any(Cliente.class))).thenReturn(criarCliente(1L));

        mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content("""
                                {"nomeCliente":"Fulano de Tal","enderecoCliente":"Rua A, 123","telefoneCliente":"11999999999"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoCliente").value(1))
                .andExpect(jsonPath("$.nomeCliente").value("Fulano de Tal"));
    }

    @Test
    void cadastrarCliente_nomeEmBranco_retorna400ComDetalheDoCampo() throws Exception {
        mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content("""
                                {"nomeCliente":"","enderecoCliente":"Rua A, 123","telefoneCliente":"11999999999"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[0]").value("nomeCliente: nomeCliente é obrigatório"));

        verify(clienteService, never()).salvarCliente(any());
    }

    @Test
    void cadastrarCliente_camposAusentes_retorna400() throws Exception {
        mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarClientes_retorna200ComListaDeClientes() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(criarCliente(1L), criarCliente(2L)));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void buscarCliente_existente_retorna200ComCliente() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(criarCliente(1L)));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoCliente").value(1));
    }

    @Test
    void buscarCliente_inexistente_retorna404() throws Exception {
        when(clienteService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void atualizarCliente_dadosValidos_retorna200ComClienteAtualizado() throws Exception {
        when(clienteService.atualizarCliente(eq(1L), any(Cliente.class))).thenReturn(criarCliente(1L));

        mockMvc.perform(put("/api/clientes/1")
                        .contentType("application/json")
                        .content("""
                                {"nomeCliente":"Fulano de Tal","enderecoCliente":"Rua A, 123","telefoneCliente":"11999999999"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoCliente").value(1));
    }

    @Test
    void atualizarCliente_inexistente_retorna404() throws Exception {
        when(clienteService.atualizarCliente(eq(99L), any(Cliente.class)))
                .thenThrow(new RecursoNaoEncontradoException("Cliente não encontrado com o código: 99"));

        mockMvc.perform(put("/api/clientes/99")
                        .contentType("application/json")
                        .content("""
                                {"nomeCliente":"Fulano de Tal","enderecoCliente":"Rua A, 123","telefoneCliente":"11999999999"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluirCliente_existente_retorna204() throws Exception {
        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).deletarCliente(1L);
    }

    @Test
    void excluirCliente_inexistente_retorna404() throws Exception {
        org.mockito.Mockito.doThrow(new RecursoNaoEncontradoException("Cliente não encontrado com o código: 99"))
                .when(clienteService).deletarCliente(99L);

        mockMvc.perform(delete("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }
}
