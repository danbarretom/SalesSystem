package com.portifolio.sistema_vendas.service;

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
        // Futuramente, suas regras de validação customizadas entram aqui
        // Exemplo: if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) throw Exception...
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

    // DELETE
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public List<Produto> listarEstoqueBaixo() {
        return produtoRepository.buscarProdutosComEstoqueBaixo();
    }
}