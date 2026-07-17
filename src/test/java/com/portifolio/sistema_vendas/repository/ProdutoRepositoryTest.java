package com.portifolio.sistema_vendas.repository;

import com.portifolio.sistema_vendas.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto criarProduto(String descricao, int estoqueAtual, int estoqueMinimo) {
        Produto produto = new Produto();
        produto.setDescricaoProduto(descricao);
        produto.setValorCompra(new BigDecimal("10.00"));
        produto.setValorVenda(new BigDecimal("20.00"));
        produto.setEstoqueAtual(estoqueAtual);
        produto.setEstoqueMinimo(estoqueMinimo);
        return entityManager.persistAndFlush(produto);
    }

    @Test
    void buscarProdutosComEstoqueBaixo_retornaApenasProdutosAbaixoDoMinimo() {
        criarProduto("Estoque baixo", 1, 5);
        criarProduto("Estoque suficiente", 10, 5);
        criarProduto("Estoque no limite exato", 5, 5);

        List<Produto> resultado = produtoRepository.buscarProdutosComEstoqueBaixo();

        assertThat(resultado)
                .extracting(Produto::getDescricaoProduto)
                .containsExactly("Estoque baixo");
    }

    @Test
    void buscarProdutosComEstoqueBaixo_semProdutosAbaixoDoMinimo_retornaListaVazia() {
        criarProduto("Estoque suficiente", 10, 5);

        assertThat(produtoRepository.buscarProdutosComEstoqueBaixo()).isEmpty();
    }

    @Test
    void save_comVersaoDesatualizada_lancaOptimisticLockingFailureException() {
        Produto produto = criarProduto("Produto disputado", 10, 2);
        Long id = produto.getCodigoProduto();

        // Simula duas transações concorrentes lendo a mesma linha: uma cópia "desatualizada"
        // é destacada do contexto de persistência antes de a outra ser salva.
        Produto copiaDesatualizada = produtoRepository.findById(id).orElseThrow();
        entityManager.detach(copiaDesatualizada);

        Produto copiaAtual = produtoRepository.findById(id).orElseThrow();
        copiaAtual.setEstoqueAtual(copiaAtual.getEstoqueAtual() - 1);
        produtoRepository.save(copiaAtual);
        entityManager.flush();

        copiaDesatualizada.setEstoqueAtual(copiaDesatualizada.getEstoqueAtual() - 5);

        assertThatThrownBy(() -> {
            produtoRepository.save(copiaDesatualizada);
            entityManager.flush();
        }).isInstanceOf(OptimisticLockingFailureException.class);
    }
}
