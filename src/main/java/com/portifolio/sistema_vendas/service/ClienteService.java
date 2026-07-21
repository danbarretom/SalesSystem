package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente atualizarCliente(Long id, Cliente dadosAtualizados) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> RecursoNaoEncontradoException.paraId("Cliente", id));

        clienteExistente.setNomeCliente(dadosAtualizados.getNomeCliente());
        clienteExistente.setEnderecoCliente(dadosAtualizados.getEnderecoCliente());
        clienteExistente.setTelefoneCliente(dadosAtualizados.getTelefoneCliente());

        return clienteRepository.save(clienteExistente);
    }

    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw RecursoNaoEncontradoException.paraId("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }
}
