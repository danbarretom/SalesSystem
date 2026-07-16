package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
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
    void salvarProduto_delegaParaRepositorySave() {
        Produto produto = criarProduto(null);
        Produto produtoSalvo = criarProduto(1L);
        when(produtoRepository.save(produto)).thenReturn(produtoSalvo);

        Produto resultado = produtoService.salvarProduto(produto);

        assertThat(resultado.getCodigoProduto()).isEqualTo(1L);
    }

    @Test
    void listarTodos_delegaParaRepository() {
        List<Produto> produtos = List.of(criarProduto(1L), criarProduto(2L));
        when(produtoRepository.findAll()).thenReturn(produtos);

        assertThat(produtoService.listarTodos()).isEqualTo(produtos);
    }

    @Test
    void buscarPorId_existente_retornaOptionalPreenchido() {
        Produto produto = criarProduto(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThat(produtoService.buscarPorId(1L)).contains(produto);
    }

    @Test
    void buscarPorId_inexistente_retornaOptionalVazio() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(produtoService.buscarPorId(99L)).isEmpty();
    }

    @Test
    void atualizarProduto_existente_atualizaTodosOsCampos() {
        Produto produtoExistente = criarProduto(1L);
        Produto dadosAtualizados = new Produto();
        dadosAtualizados.setDescricaoProduto("Teclado Mecânico");
        dadosAtualizados.setValorCompra(new BigDecimal("120.00"));
        dadosAtualizados.setValorVenda(new BigDecimal("250.00"));
        dadosAtualizados.setEstoqueAtual(5);
        dadosAtualizados.setEstoqueMinimo(1);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto resultado = produtoService.atualizarProduto(1L, dadosAtualizados);

        assertThat(resultado.getCodigoProduto()).isEqualTo(1L);
        assertThat(resultado.getDescricaoProduto()).isEqualTo("Teclado Mecânico");
        assertThat(resultado.getValorCompra()).isEqualByComparingTo(new BigDecimal("120.00"));
        assertThat(resultado.getValorVenda()).isEqualByComparingTo(new BigDecimal("250.00"));
        assertThat(resultado.getEstoqueAtual()).isEqualTo(5);
        assertThat(resultado.getEstoqueMinimo()).isEqualTo(1);
    }

    @Test
    void atualizarProduto_inexistente_lancaRecursoNaoEncontradoENaoSalva() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.atualizarProduto(99L, criarProduto(null)))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deletarProduto_existente_chamaDeleteById() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.deletarProduto(1L);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(produtoRepository).deleteById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(1L);
    }

    @Test
    void deletarProduto_inexistente_lancaRecursoNaoEncontradoENaoDeleta() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> produtoService.deletarProduto(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(produtoRepository, never()).deleteById(any());
    }

    @Test
    void listarEstoqueBaixo_delegaParaQueryCustomizadaDoRepository() {
        List<Produto> produtosEstoqueBaixo = List.of(criarProduto(1L));
        when(produtoRepository.buscarProdutosComEstoqueBaixo()).thenReturn(produtosEstoqueBaixo);

        assertThat(produtoService.listarEstoqueBaixo()).isEqualTo(produtosEstoqueBaixo);
    }
}
