package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    private Produto criarProduto(Long id) {
        Produto produto = new Produto();
        produto.setCodigoProduto(id);
        produto.setDescricaoProduto("Mouse Gamer");
        produto.setValorCompra(new BigDecimal("50.00"));
        produto.setValorVenda(new BigDecimal("100.00"));
        produto.setEstoqueAtual(10);
        produto.setEstoqueMinimo(2);
        return produto;
    }

    @Test
    void cadastrarProduto_dadosValidos_retorna201ComProdutoCriado() throws Exception {
        when(produtoService.salvarProduto(any(Produto.class))).thenReturn(criarProduto(1L));

        mockMvc.perform(post("/api/produtos")
                        .contentType("application/json")
                        .content("""
                                {"descricaoProduto":"Mouse Gamer","valorCompra":50.00,"valorVenda":100.00,"estoqueAtual":10,"estoqueMinimo":2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoProduto").value(1));
    }

    @Test
    void cadastrarProduto_valorVendaZero_retorna400() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType("application/json")
                        .content("""
                                {"descricaoProduto":"Mouse Gamer","valorCompra":50.00,"valorVenda":0,"estoqueAtual":10,"estoqueMinimo":2}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[0]").value("valorVenda: valorVenda deve ser maior que zero"));
    }

    @Test
    void cadastrarProduto_estoqueAtualNegativo_retorna400() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType("application/json")
                        .content("""
                                {"descricaoProduto":"Mouse Gamer","valorCompra":50.00,"valorVenda":100.00,"estoqueAtual":-1,"estoqueMinimo":2}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarProdutos_retorna200ComListaDeProdutos() throws Exception {
        when(produtoService.listarTodos()).thenReturn(List.of(criarProduto(1L), criarProduto(2L)));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listarEstoqueBaixo_retorna200ComProdutosAbaixoDoMinimo() throws Exception {
        when(produtoService.listarEstoqueBaixo()).thenReturn(List.of(criarProduto(1L)));

        mockMvc.perform(get("/api/produtos/estoque-baixo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarProduto_existente_retorna200ComProduto() throws Exception {
        when(produtoService.buscarPorId(1L)).thenReturn(Optional.of(criarProduto(1L)));

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoProduto").value(1));
    }

    @Test
    void buscarProduto_inexistente_retorna404() throws Exception {
        when(produtoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarProduto_dadosValidos_retorna200ComProdutoAtualizado() throws Exception {
        when(produtoService.atualizarProduto(eq(1L), any(Produto.class))).thenReturn(criarProduto(1L));

        mockMvc.perform(put("/api/produtos/1")
                        .contentType("application/json")
                        .content("""
                                {"descricaoProduto":"Mouse Gamer","valorCompra":50.00,"valorVenda":100.00,"estoqueAtual":10,"estoqueMinimo":2}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void atualizarProduto_inexistente_retorna404() throws Exception {
        when(produtoService.atualizarProduto(eq(99L), any(Produto.class)))
                .thenThrow(new RecursoNaoEncontradoException("Produto não encontrado com o código: 99"));

        mockMvc.perform(put("/api/produtos/99")
                        .contentType("application/json")
                        .content("""
                                {"descricaoProduto":"Mouse Gamer","valorCompra":50.00,"valorVenda":100.00,"estoqueAtual":10,"estoqueMinimo":2}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarProduto_conflitoDeVersao_retorna409() throws Exception {
        when(produtoService.atualizarProduto(eq(1L), any(Produto.class)))
                .thenThrow(new OptimisticLockingFailureException("Row was updated or deleted by another transaction"));

        mockMvc.perform(put("/api/produtos/1")
                        .contentType("application/json")
                        .content("""
                                {"descricaoProduto":"Mouse Gamer","valorCompra":50.00,"valorVenda":100.00,"estoqueAtual":10,"estoqueMinimo":2}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void excluirProduto_existente_retorna204() throws Exception {
        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluirProduto_inexistente_retorna404() throws Exception {
        org.mockito.Mockito.doThrow(new RecursoNaoEncontradoException("Produto não encontrado com o código: 99"))
                .when(produtoService).deletarProduto(99L);

        mockMvc.perform(delete("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }
}
