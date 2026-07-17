package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.Cliente;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "nomeCliente é obrigatório") String nomeCliente,
        @NotBlank(message = "enderecoCliente é obrigatório") String enderecoCliente,
        @NotBlank(message = "telefoneCliente é obrigatório") String telefoneCliente
) {
    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente(nomeCliente);
        cliente.setEnderecoCliente(enderecoCliente);
        cliente.setTelefoneCliente(telefoneCliente);
        return cliente;
    }
}
