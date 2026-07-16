package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VendaResponse(
        Long id,
        LocalDate dataVenda,
        ClienteResponse cliente,
        BigDecimal valorTotal,
        LocalDate dataVencimento,
        TipoVenda tipoVenda,
        List<ItemVendaResponse> itens
) {
    public static VendaResponse from(Venda venda) {
        return new VendaResponse(
                venda.getId(),
                venda.getDataVenda(),
                venda.getCliente() != null ? ClienteResponse.from(venda.getCliente()) : null,
                venda.getValorTotal(),
                venda.getDataVencimento(),
                venda.getTipoVenda(),
                venda.getItens().stream().map(ItemVendaResponse::from).toList()
        );
    }
}
