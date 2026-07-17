package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    // A dependência do nosso banco de dados
    private final ProdutoRepository produtoRepository;

    // Injeção de dependência via Construtor (A melhor prática recomendada pelo Spring)
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // CREATE / UPDATE
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    // READ (Todos)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // READ (Por ID)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // UPDATE (por ID)
    public Produto atualizarProduto(Long id, Produto dadosAtualizados) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o código: " + id));

        produtoExistente.setDescricaoProduto(dadosAtualizados.getDescricaoProduto());
        produtoExistente.setValorCompra(dadosAtualizados.getValorCompra());
        produtoExistente.setValorVenda(dadosAtualizados.getValorVenda());
        produtoExistente.setEstoqueAtual(dadosAtualizados.getEstoqueAtual());
        produtoExistente.setEstoqueMinimo(dadosAtualizados.getEstoqueMinimo());

        return produtoRepository.save(produtoExistente);
    }

    // DELETE
    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado com o código: " + id);
        }
        produtoRepository.deleteById(id);
    }

    public List<Produto> listarEstoqueBaixo() {
        return produtoRepository.buscarProdutosComEstoqueBaixo();
    }
}