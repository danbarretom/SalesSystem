package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.dto.ProdutoRequest;
import com.portifolio.sistema_vendas.dto.ProdutoResponse;
import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.service.ProdutoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProdutoResponse> cadastrarProduto(@Valid @RequestBody ProdutoRequest request) {
        Produto produtoSalvo = produtoService.salvarProduto(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponse.from(produtoSalvo));
    }

    // Rota GET para listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarProdutos() {
        List<ProdutoResponse> produtos = produtoService.listarTodos().stream()
                .map(ProdutoResponse::from)
                .toList();
        return ResponseEntity.ok(produtos);
    }

    // Rota GET para listar produtos com estoque abaixo do mínimo
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoResponse>> listarEstoqueBaixo() {
        List<ProdutoResponse> produtos = produtoService.listarEstoqueBaixo().stream()
                .map(ProdutoResponse::from)
                .toList();
        return ResponseEntity.ok(produtos);
    }

    // Rota GET para buscar um produto por id
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProduto(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o código: " + id));
        return ResponseEntity.ok(ProdutoResponse.from(produto));
    }

    // Rota PUT para atualizar um produto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        Produto produtoAtualizado = produtoService.atualizarProduto(id, request.toEntity());
        return ResponseEntity.ok(ProdutoResponse.from(produtoAtualizado));
    }

    // Rota DELETE para excluir um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
