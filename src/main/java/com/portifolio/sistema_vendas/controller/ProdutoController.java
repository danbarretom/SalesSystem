package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    // Injeção da nossa camada de negócios
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Rota POST para criar um novo produto
    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto) {
        Produto produtoSalvo = produtoService.salvarProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    // Rota GET para listar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    // Rota GET para listar produtos com estoque abaixo do mínimo
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> listarEstoqueBaixo() {
        List<Produto> produtos = produtoService.listarEstoqueBaixo();
        return ResponseEntity.ok(produtos);
    }
}