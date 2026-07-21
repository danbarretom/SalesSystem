package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    // A dependência do nosso banco de dados
    private final ClienteRepository clienteRepository;

    // Injeção de dependência via Construtor (A melhor prática recomendada pelo Spring)
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // CREATE / UPDATE
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // READ (Todos)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // READ (Por ID)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // UPDATE (por ID)
    public Cliente atualizarCliente(Long id, Cliente dadosAtualizados) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> RecursoNaoEncontradoException.paraId("Cliente", id));

        clienteExistente.setNomeCliente(dadosAtualizados.getNomeCliente());
        clienteExistente.setEnderecoCliente(dadosAtualizados.getEnderecoCliente());
        clienteExistente.setTelefoneCliente(dadosAtualizados.getTelefoneCliente());

        return clienteRepository.save(clienteExistente);
    }

    // DELETE
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw RecursoNaoEncontradoException.paraId("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }
}
