package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "descricaoProduto é obrigatório") @Size(max = 255, message = "descricaoProduto deve ter no máximo 255 caracteres") String descricaoProduto,
        @NotNull(message = "valorCompra é obrigatório") @PositiveOrZero(message = "valorCompra não pode ser negativo") BigDecimal valorCompra,
        @NotNull(message = "valorVenda é obrigatório") @Positive(message = "valorVenda deve ser maior que zero") BigDecimal valorVenda,
        @NotNull(message = "estoqueAtual é obrigatório") @PositiveOrZero(message = "estoqueAtual não pode ser negativo") Integer estoqueAtual,
        @NotNull(message = "estoqueMinimo é obrigatório") @PositiveOrZero(message = "estoqueMinimo não pode ser negativo") Integer estoqueMinimo
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        produto.setDescricaoProduto(descricaoProduto);
        produto.setValorCompra(valorCompra);
        produto.setValorVenda(valorVenda);
        produto.setEstoqueAtual(estoqueAtual);
        produto.setEstoqueMinimo(estoqueMinimo);
        return produto;
    }
}
