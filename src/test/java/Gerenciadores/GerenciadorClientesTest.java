package Gerenciadores;

import Modelos.Cliente;
import Excecoes.EntidadeNaoEncontradaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GerenciadorClientesTest {

    private GerenciadorClientes gerenciador;
    private final String ARQUIVO_TESTE = "clientes_teste.txt";

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorClientes(ARQUIVO_TESTE);
    }

    @AfterEach
    void tearDown() {
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            boolean deletado = arquivo.delete();
            if (!deletado) System.err.println("Aviso: Não foi possível apagar o arquivo.");
        }
    }

    @Test
    void deveCadastrarEGerarCodigoSequencial() {
        Cliente c1 = new Cliente(0, "João", "Rua A", "123");
        gerenciador.cadastrarNovoCliente(c1);

        assertTrue(c1.getCodigoCliente() > 0, "O código deve ser gerado automaticamente.");
        // Aqui o aviso do getLast() vai sumir:
        assertEquals("João", gerenciador.lista.getLast().getNomeCliente());
    }

    @Test
    void deveLancarErroAoConsultarClienteInexistente() {
        assertThrows(EntidadeNaoEncontradaException.class, () -> gerenciador.consultarCliente(999));
    }

    @Test
    void deveGerarLinhaEReconstruirClienteCorretamente() {
        Cliente original = new Cliente(3, "Maria", "Av. Central, 100", "99999-0000");

        String linha = gerenciador.gerarLinhaDoObjeto(original);
        Cliente reconstruido = gerenciador.criarObjetoDaLinha(linha);

        assertEquals(original.getCodigoCliente(), reconstruido.getCodigoCliente());
        assertEquals(original.getNomeCliente(), reconstruido.getNomeCliente());
        assertEquals(original.getEnderecoCliente(), reconstruido.getEnderecoCliente());
        assertEquals(original.getTelefoneCliente(), reconstruido.getTelefoneCliente());
    }
}