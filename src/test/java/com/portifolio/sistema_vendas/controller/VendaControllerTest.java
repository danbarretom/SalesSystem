package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.exception.RegraNegocioException;
import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
import com.portifolio.sistema_vendas.service.VendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendaController.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendaService vendaService;

    private Venda criarVendaSalva() {
        Produto produto = new Produto();
        produto.setCodigoProduto(1L);
        produto.setDescricaoProduto("Mouse Gamer");
        produto.setValorCompra(new BigDecimal("50.00"));
        produto.setValorVenda(new BigDecimal("100.00"));
        produto.setEstoqueAtual(8);
        produto.setEstoqueMinimo(2);

        ItemVenda item = new ItemVenda();
        item.setId(1L);
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setSubtotal(new BigDecimal("200.00"));

        Venda venda = new Venda();
        venda.setId(1L);
        venda.setDataVenda(LocalDate.now());
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.setValorTotal(new BigDecimal("200.00"));
        venda.adicionarItem(item);
        return venda;
    }

    @Test
    void registrarVenda_aVistaValida_retorna201ComVendaCriada() throws Exception {
        when(vendaService.salvarVenda(any(Venda.class))).thenReturn(criarVendaSalva());

        mockMvc.perform(post("/api/vendas")
                        .contentType("application/json")
                        .content("""
                                {"tipoVenda":"A_VISTA","itens":[{"codigoProduto":1,"quantidade":2}]}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valorTotal").value(200.00))
                .andExpect(jsonPath("$.itens.length()").value(1));
    }

    @Test
    void registrarVenda_semTipoVenda_retorna400() throws Exception {
        mockMvc.perform(post("/api/vendas")
                        .contentType("application/json")
                        .content("""
                                {"itens":[{"codigoProduto":1,"quantidade":2}]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarVenda_itensVazio_retorna400() throws Exception {
        mockMvc.perform(post("/api/vendas")
                        .contentType("application/json")
                        .content("""
                                {"tipoVenda":"A_VISTA","itens":[]}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[0]").value("itens: itens não pode ser vazio"));
    }

    @Test
    void registrarVenda_quantidadeInvalidaNoItem_retorna400() throws Exception {
        mockMvc.perform(post("/api/vendas")
                        .contentType("application/json")
                        .content("""
                                {"tipoVenda":"A_VISTA","itens":[{"codigoProduto":1,"quantidade":0}]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarVenda_regraDeNegocioViolada_retorna400() throws Exception {
        when(vendaService.salvarVenda(any(Venda.class)))
                .thenThrow(new RegraNegocioException("Vendas a prazo exigem Cliente e Data de Vencimento!"));

        mockMvc.perform(post("/api/vendas")
                        .contentType("application/json")
                        .content("""
                                {"tipoVenda":"A_PRAZO","itens":[{"codigoProduto":1,"quantidade":2}]}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Vendas a prazo exigem Cliente e Data de Vencimento!"));
    }

    @Test
    void listarVendas_retorna200ComListaDeVendas() throws Exception {
        when(vendaService.listarTodas()).thenReturn(List.of(criarVendaSalva()));

        mockMvc.perform(get("/api/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarVendasPorPeriodo_comIntervaloValido_retorna200() throws Exception {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        when(vendaService.buscarVendasPorPeriodo(inicio, fim)).thenReturn(List.of(criarVendaSalva()));

        mockMvc.perform(get("/api/vendas/periodo")
                        .param("inicio", "2026-01-01")
                        .param("fim", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarVendasPorPeriodo_semParametroObrigatorio_retorna400() throws Exception {
        mockMvc.perform(get("/api/vendas/periodo").param("inicio", "2026-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Parâmetro obrigatório ausente"));
    }

    @Test
    void buscarVendasPorPeriodo_comDataEmFormatoInvalido_retorna400() throws Exception {
        mockMvc.perform(get("/api/vendas/periodo")
                        .param("inicio", "data-invalida")
                        .param("fim", "2026-01-31"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Parâmetro inválido"));
    }

    @Test
    void registrarVenda_corpoDaRequisicaoMalFormado_retorna400() throws Exception {
        mockMvc.perform(post("/api/vendas")
                        .contentType("application/json")
                        .content("{ isso não é json válido"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Corpo da requisição inválido"));
    }
}
