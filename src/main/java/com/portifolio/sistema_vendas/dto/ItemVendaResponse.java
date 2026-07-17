package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.ItemVenda;

import java.math.BigDecimal;

public record ItemVendaResponse(
        Long id,
        ProdutoResponse produto,
        Integer quantidade,
        BigDecimal subtotal
) {
    public static ItemVendaResponse from(ItemVenda item) {
        return new ItemVendaResponse(
                item.getId(),
                ProdutoResponse.from(item.getProduto()),
                item.getQuantidade(),
                item.getSubtotal()
        );
    }
}
