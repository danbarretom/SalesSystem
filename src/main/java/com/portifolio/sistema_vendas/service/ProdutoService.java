package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto atualizarProduto(Long id, Produto dadosAtualizados) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> RecursoNaoEncontradoException.paraId("Produto", id));

        produtoExistente.setDescricaoProduto(dadosAtualizados.getDescricaoProduto());
        produtoExistente.setValorCompra(dadosAtualizados.getValorCompra());
        produtoExistente.setValorVenda(dadosAtualizados.getValorVenda());
        produtoExistente.setEstoqueAtual(dadosAtualizados.getEstoqueAtual());
        produtoExistente.setEstoqueMinimo(dadosAtualizados.getEstoqueMinimo());

        return produtoRepository.save(produtoExistente);
    }

    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw RecursoNaoEncontradoException.paraId("Produto", id);
        }
        produtoRepository.deleteById(id);
    }

    public List<Produto> listarEstoqueBaixo() {
        return produtoRepository.buscarProdutosComEstoqueBaixo();
    }
}