package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
import com.portifolio.sistema_vendas.repository.ClienteRepository;
import com.portifolio.sistema_vendas.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração ponta a ponta (Spring context real + H2 real, sem mocks), focados em
 * comportamentos que só se manifestam com Hibernate de verdade: o decremento de estoque de
 * VendaService depende inteiramente do dirty checking automático (não há um produtoRepository.save()
 * explícito no código), e o bug de associação corrigido anteriormente só é detectável reconsultando
 * o banco após um flush+clear — checar apenas a instância em memória mascararia uma regressão.
 */
@SpringBootTest
@Transactional
class VendaServiceIntegrationTest {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EntityManager entityManager;

    private Produto persistirProduto(String descricao, int estoqueAtual) {
        Produto produto = new Produto();
        produto.setDescricaoProduto(descricao);
        produto.setValorCompra(new BigDecimal("50.00"));
        produto.setValorVenda(new BigDecimal("100.00"));
        produto.setEstoqueAtual(estoqueAtual);
        produto.setEstoqueMinimo(2);
        return produtoRepository.save(produto);
    }

    private Cliente persistirCliente() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Fulano de Tal");
        cliente.setEnderecoCliente("Rua A, 123");
        cliente.setTelefoneCliente("11999999999");
        return clienteRepository.save(cliente);
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
    void salvarVenda_decrementoDeEstoquePersisteDeVerdade_naoApenasEmMemoria() {
        Produto produto = persistirProduto("Mouse Gamer", 10);

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.adicionarItem(criarItemRequisicao(produto.getCodigoProduto(), 3));

        vendaService.salvarVenda(venda);

        entityManager.flush();
        entityManager.clear();

        Produto produtoRecarregado = produtoRepository.findById(produto.getCodigoProduto()).orElseThrow();
        assertThat(produtoRecarregado.getEstoqueAtual()).isEqualTo(7);
    }

    @Test
    void salvarVenda_itemPersistidoReferenciaOProdutoCorreto_naoOObjetoDetachadoDaRequisicao() {
        Produto produtoCorreto = persistirProduto("Mouse Gamer", 10);
        Produto outroProduto = persistirProduto("Teclado Mecânico", 5);

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_VISTA);
        // Item de requisição só carrega o código do produto (como um DTO desserializado faria) —
        // a referência real deve ser resolvida pelo service, não vir do objeto detachado.
        venda.adicionarItem(criarItemRequisicao(produtoCorreto.getCodigoProduto(), 1));

        Venda salva = vendaService.salvarVenda(venda);

        entityManager.flush();
        entityManager.clear();

        Venda vendaRecarregada = entityManager.find(Venda.class, salva.getId());
        assertThat(vendaRecarregada.getItens()).hasSize(1);
        assertThat(vendaRecarregada.getItens().get(0).getProduto().getCodigoProduto())
                .isEqualTo(produtoCorreto.getCodigoProduto())
                .isNotEqualTo(outroProduto.getCodigoProduto());
    }

    @Test
    void salvarVenda_aPrazo_clientePersistidoReferenciaOClienteCorreto_naoOObjetoDetachadoDaRequisicao() {
        Produto produto = persistirProduto("Mouse Gamer", 10);
        Cliente clienteCorreto = persistirCliente();

        Cliente clienteReferencia = new Cliente();
        clienteReferencia.setCodigoCliente(clienteCorreto.getCodigoCliente());

        Venda venda = new Venda();
        venda.setTipoVenda(TipoVenda.A_PRAZO);
        venda.setCliente(clienteReferencia);
        venda.setDataVencimento(LocalDate.now().plusDays(30));
        venda.adicionarItem(criarItemRequisicao(produto.getCodigoProduto(), 1));

        Venda salva = vendaService.salvarVenda(venda);

        entityManager.flush();
        entityManager.clear();

        Venda vendaRecarregada = entityManager.find(Venda.class, salva.getId());
        assertThat(vendaRecarregada.getCliente().getCodigoCliente()).isEqualTo(clienteCorreto.getCodigoCliente());
        assertThat(vendaRecarregada.getCliente().getNomeCliente()).isEqualTo("Fulano de Tal");
    }

    @Test
    void salvarVenda_multiplosItensNoMesmoProduto_decrementaEstoqueAcumulado() {
        Produto produto = persistirProduto("Mouse Gamer", 10);

        Venda venda1 = new Venda();
        venda1.setTipoVenda(TipoVenda.A_VISTA);
        venda1.adicionarItem(criarItemRequisicao(produto.getCodigoProduto(), 3));
        vendaService.salvarVenda(venda1);

        Venda venda2 = new Venda();
        venda2.setTipoVenda(TipoVenda.A_VISTA);
        venda2.adicionarItem(criarItemRequisicao(produto.getCodigoProduto(), 2));
        vendaService.salvarVenda(venda2);

        entityManager.flush();
        entityManager.clear();

        Produto produtoRecarregado = produtoRepository.findById(produto.getCodigoProduto()).orElseThrow();
        assertThat(produtoRecarregado.getEstoqueAtual()).isEqualTo(5);
    }
}
