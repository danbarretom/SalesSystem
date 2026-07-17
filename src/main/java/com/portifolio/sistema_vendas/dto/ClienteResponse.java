package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.Cliente;

public record ClienteResponse(
        Long codigoCliente,
        String nomeCliente,
        String enderecoCliente,
        String telefoneCliente
) {
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getCodigoCliente(),
                cliente.getNomeCliente(),
                cliente.getEnderecoCliente(),
                cliente.getTelefoneCliente()
        );
    }
}
