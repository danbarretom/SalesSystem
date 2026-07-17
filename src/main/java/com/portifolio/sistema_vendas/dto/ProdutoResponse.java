package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long codigoProduto,
        String descricaoProduto,
        BigDecimal valorCompra,
        BigDecimal valorVenda,
        int estoqueAtual,
        int estoqueMinimo
) {
    public static ProdutoResponse from(Produto produto) {
        return new ProdutoResponse(
                produto.getCodigoProduto(),
                produto.getDescricaoProduto(),
                produto.getValorCompra(),
                produto.getValorVenda(),
                produto.getEstoqueAtual(),
                produto.getEstoqueMinimo()
        );
    }
}
