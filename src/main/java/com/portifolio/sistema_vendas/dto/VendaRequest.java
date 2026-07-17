package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record VendaRequest(
        @NotNull(message = "tipoVenda é obrigatório") TipoVenda tipoVenda,
        Long codigoCliente,
        LocalDate dataVencimento,
        @NotEmpty(message = "itens não pode ser vazio") @Valid List<ItemVendaRequest> itens
) {
    public Venda toEntity() {
        Venda venda = new Venda();
        venda.setTipoVenda(tipoVenda);
        venda.setDataVencimento(dataVencimento);

        if (codigoCliente != null) {
            Cliente clienteReferencia = new Cliente();
            clienteReferencia.setCodigoCliente(codigoCliente);
            venda.setCliente(clienteReferencia);
        }

        for (ItemVendaRequest itemRequest : itens) {
            ItemVenda item = itemRequest.toEntity();
            venda.adicionarItem(item);
        }

        return venda;
    }
}
