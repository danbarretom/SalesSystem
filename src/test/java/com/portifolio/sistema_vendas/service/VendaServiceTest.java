package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.exception.RegraNegocioException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
import com.portifolio.sistema_vendas.repository.ClienteRepository;
import com.portifolio.sistema_vendas.repository.ProdutoRepository;
import com.portifolio.sistema_vendas.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VendaService vendaService;

    private Produto produtoBanco;

    @BeforeEach
    void setUp() {
        produtoBanco = new Produto();
        produtoBanco.setCodigoProduto(1L);
        produtoBanco.setDescricaoProduto("Mouse Gamer");
        produtoBanco.setValorVenda(new BigDecimal("100.00"));
        produtoBanco.setEstoqueAtual(10);
        produtoBanco.setEstoqueMinimo(2);
    }

    private ItemVenda criarItemRequisicao(Long codigoProduto, int quantidade) {
        Produto produtoReferencia = new Produto();
        produtoReferencia.setCodigoProduto(codigoProduto);
        ItemVenda item = new ItemVenda();
        item.setProduto(produtoReferencia);
        item.setQuantidade(quantidade);
        return item;
    }

    @Test
    void salvarVenda_aVista_calculaTotalEDecrementaEstoque() {
        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.adicionarItem(criarItemRequisicao(1L, 3));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoBanco));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda salva = vendaService.salvarVenda(venda);

        assertThat(salva.getValorTotal()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(produtoBanco.getEstoqueAtual()).isEqualTo(7);
        assertThat(salva.getDataVenda()).isEqualTo(LocalDate.now());
        verifyNoInteractions(clienteRepository);
    }

    @Test
    void salvarVenda_associaProdutoGerenciadoAoItem_naoOProdutoDetachadoDaRequisicao() {
        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        ItemVenda itemRequisicao = criarItemRequisicao(1L, 1);
        venda.adicionarItem(itemRequisicao);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoBanco));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        vendaService.salvarVenda(venda);

        assertThat(itemRequisicao.getProduto()).isSameAs(produtoBanco);
    }

    @Test
    void salvarVenda_multiplosItens_somaSubtotaisNoTotal() {
        Produto produto2 = new Produto();
        produto2.setCodigoProduto(2L);
        produto2.setValorVenda(new BigDecimal("50.00"));
        produto2.setEstoqueAtual(5);
        produto2.setEstoqueMinimo(1);

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.adicionarItem(criarItemRequisicao(1L, 2));
        venda.adicionarItem(criarItemRequisicao(2L, 1));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoBanco));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda salva = vendaService.salvarVenda(venda);

        assertThat(salva.getValorTotal()).isEqualByComparingTo(new BigDecimal("250.00"));
    }

    @Test
    void salvarVenda_estoqueInsuficiente_lancaRegraNegocioENaoSalva() {
        produtoBanco.setEstoqueAtual(2);
        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.adicionarItem(criarItemRequisicao(1L, 5));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoBanco));

        assertThatThrownBy(() -> vendaService.salvarVenda(venda))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("Estoque insuficiente");

        verify(vendaRepository, never()).save(any());
    }

    @Test
    void salvarVenda_produtoNaoEncontrado_lancaRecursoNaoEncontrado() {
        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.adicionarItem(criarItemRequisicao(99L, 1));

        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendaService.salvarVenda(venda))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void salvarVenda_aPrazoSemCliente_lancaRegraNegocioSemTocarProduto() {
        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_PRAZO);
        venda.setDataVencimento(LocalDate.now().plusDays(30));
        venda.adicionarItem(criarItemRequisicao(1L, 1));

        assertThatThrownBy(() -> vendaService.salvarVenda(venda))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("Cliente e Data de Vencimento");

        verifyNoInteractions(produtoRepository);
    }

    @Test
    void salvarVenda_aPrazoSemDataVencimento_lancaRegraNegocio() {
        Cliente clienteReferencia = new Cliente();
        clienteReferencia.setCodigoCliente(1L);

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_PRAZO);
        venda.setCliente(clienteReferencia);
        venda.adicionarItem(criarItemRequisicao(1L, 1));

        assertThatThrownBy(() -> vendaService.salvarVenda(venda))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void salvarVenda_aPrazoClienteNaoEncontrado_lancaRecursoNaoEncontradoSemTocarProduto() {
        Cliente clienteReferencia = new Cliente();
        clienteReferencia.setCodigoCliente(42L);

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_PRAZO);
        venda.setCliente(clienteReferencia);
        venda.setDataVencimento(LocalDate.now().plusDays(10));
        venda.adicionarItem(criarItemRequisicao(1L, 1));

        when(clienteRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendaService.salvarVenda(venda))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verifyNoInteractions(produtoRepository);
    }

    @Test
    void salvarVenda_aPrazoValida_associaClienteGerenciadoNaoODetachadoDaRequisicao() {
        Cliente clienteReferencia = new Cliente();
        clienteReferencia.setCodigoCliente(7L);

        Cliente clienteBanco = new Cliente();
        clienteBanco.setCodigoCliente(7L);
        clienteBanco.setNomeCliente("Fulano de Tal");

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_PRAZO);
        venda.setCliente(clienteReferencia);
        venda.setDataVencimento(LocalDate.now().plusDays(15));
        venda.adicionarItem(criarItemRequisicao(1L, 1));

        when(clienteRepository.findById(7L)).thenReturn(Optional.of(clienteBanco));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoBanco));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda salva = vendaService.salvarVenda(venda);

        assertThat(salva.getCliente()).isSameAs(clienteBanco);
    }

    @Test
    void listarTodas_delegaParaRepository() {
        List<Venda> vendas = List.of(new Venda());
        when(vendaRepository.findAll()).thenReturn(vendas);

        assertThat(vendaService.listarTodas()).isEqualTo(vendas);
    }

    @Test
    void buscarVendasPorPeriodo_delegaParaRepository() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        List<Venda> vendas = List.of(new Venda());
        when(vendaRepository.findByDataVendaBetween(inicio, fim)).thenReturn(vendas);

        assertThat(vendaService.buscarVendasPorPeriodo(inicio, fim)).isEqualTo(vendas);
    }
}
