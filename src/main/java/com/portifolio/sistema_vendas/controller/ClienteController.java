package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.dto.ClienteRequest;
import com.portifolio.sistema_vendas.dto.ClienteResponse;
import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrarCliente(@Valid @RequestBody ClienteRequest request) {
        Cliente clienteSalvo = clienteService.salvarCliente(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponse.from(clienteSalvo));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(ClienteResponse::from)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o código: " + id));
        return ResponseEntity.ok(ClienteResponse.from(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        Cliente clienteAtualizado = clienteService.atualizarCliente(id, request.toEntity());
        return ResponseEntity.ok(ClienteResponse.from(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
