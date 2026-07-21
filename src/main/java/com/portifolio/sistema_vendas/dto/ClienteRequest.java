package com.portifolio.sistema_vendas.dto;

import com.portifolio.sistema_vendas.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "nomeCliente é obrigatório") @Size(max = 255, message = "nomeCliente deve ter no máximo 255 caracteres") String nomeCliente,
        @NotBlank(message = "enderecoCliente é obrigatório") @Size(max = 255, message = "enderecoCliente deve ter no máximo 255 caracteres") String enderecoCliente,
        @NotBlank(message = "telefoneCliente é obrigatório") @Size(max = 255, message = "telefoneCliente deve ter no máximo 255 caracteres") String telefoneCliente
) {
    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente(nomeCliente);
        cliente.setEnderecoCliente(enderecoCliente);
        cliente.setTelefoneCliente(telefoneCliente);
        return cliente;
    }
}
