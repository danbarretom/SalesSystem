package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente criarCliente(Long id) {
        Cliente cliente = new Cliente();
        cliente.setCodigoCliente(id);
        cliente.setNomeCliente("Fulano de Tal");
        cliente.setEnderecoCliente("Rua A, 123");
        cliente.setTelefoneCliente("11999999999");
        return cliente;
    }

    @Test
    void salvarCliente_delegaParaRepositorySave() {
        Cliente cliente = criarCliente(null);
        Cliente clienteSalvo = criarCliente(1L);
        when(clienteRepository.save(cliente)).thenReturn(clienteSalvo);

        Cliente resultado = clienteService.salvarCliente(cliente);

        assertThat(resultado.getCodigoCliente()).isEqualTo(1L);
    }

    @Test
    void listarTodos_delegaParaRepository() {
        List<Cliente> clientes = List.of(criarCliente(1L), criarCliente(2L));
        when(clienteRepository.findAll()).thenReturn(clientes);

        assertThat(clienteService.listarTodos()).isEqualTo(clientes);
    }

    @Test
    void buscarPorId_existente_retornaOptionalPreenchido() {
        Cliente cliente = criarCliente(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertThat(clienteService.buscarPorId(1L)).contains(cliente);
    }

    @Test
    void buscarPorId_inexistente_retornaOptionalVazio() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(clienteService.buscarPorId(99L)).isEmpty();
    }

    @Test
    void atualizarCliente_existente_atualizaCamposEPersisteEntidadeGerenciada() {
        Cliente clienteExistente = criarCliente(1L);
        Cliente dadosAtualizados = new Cliente();
        dadosAtualizados.setNomeCliente("Novo Nome");
        dadosAtualizados.setEnderecoCliente("Rua Nova, 456");
        dadosAtualizados.setTelefoneCliente("11888888888");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = clienteService.atualizarCliente(1L, dadosAtualizados);

        assertThat(resultado.getCodigoCliente()).isEqualTo(1L);
        assertThat(resultado.getNomeCliente()).isEqualTo("Novo Nome");
        assertThat(resultado.getEnderecoCliente()).isEqualTo("Rua Nova, 456");
        assertThat(resultado.getTelefoneCliente()).isEqualTo("11888888888");
    }

    @Test
    void atualizarCliente_inexistente_lancaRecursoNaoEncontradoENaoSalva() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.atualizarCliente(99L, criarCliente(null)))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deletarCliente_existente_chamaDeleteById() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.deletarCliente(1L);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(clienteRepository).deleteById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(1L);
    }

    @Test
    void deletarCliente_inexistente_lancaRecursoNaoEncontradoENaoDeleta() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.deletarCliente(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(clienteRepository, never()).deleteById(any());
    }
}
