package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.Produto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemVendaRequest(
        @NotNull(message = "codigoProduto é obrigatório") Long codigoProduto,
        @NotNull(message = "quantidade é obrigatória") @Positive(message = "quantidade deve ser maior que zero") Integer quantidade
) {
    public ItemVenda toEntity() {
        Produto produtoReferencia = new Produto();
        produtoReferencia.setCodigoProduto(codigoProduto);

        ItemVenda item = new ItemVenda();
        item.setProduto(produtoReferencia);
        item.setQuantidade(quantidade);
        return item;
    }
}
