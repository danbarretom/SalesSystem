package Gerenciadores;

import Modelos.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GerenciadorProdutosTest {

    private GerenciadorProdutos gerenciador;
    private final String ARQUIVO_TESTE = "produtos_teste.txt";

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorProdutos(ARQUIVO_TESTE);
    }

    @AfterEach
    void tearDown() {
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            boolean deletado = arquivo.delete();
            if (!deletado) {
                System.err.println("Aviso: Não foi possível apagar o arquivo de teste.");
            }
        }
    }

    @Test
    void deveCadastrarNovoProdutoComSucesso() {
        Produto p1 = new Produto();
        p1.setDescricaoProduto("Monitor 24 Pol");
        p1.setValorCompra(500.0);
        p1.setValorVenda(800.0);
        p1.setEstoqueAtual(10);
        p1.setEstoqueMinimo(2);

        gerenciador.cadastrarNovoProduto(p1);

        Produto salvo = gerenciador.buscarProduto(1);
        assertNotNull(salvo, "O produto deveria ter sido encontrado no banco.");
        assertEquals("Monitor 24 Pol", salvo.getDescricaoProduto(), "A descrição salva não corresponde.");
    }

    @Test
    void deveBaixarEstoqueCorretamente() {

        Produto p1 = new Produto();
        p1.setDescricaoProduto("Teclado Mecânico");
        p1.setEstoqueAtual(5);
        gerenciador.cadastrarNovoProduto(p1);

        gerenciador.baixarEstoque(1, 2);

        Produto atualizado = gerenciador.buscarProduto(1);
        assertEquals(3, atualizado.getEstoqueAtual(), "O estoque atual deveria ser atualizado para 3.");
    }

    @Test
    void deveLancarExcecaoAoBaixarEstoqueInsuficiente() {
        Produto p1 = new Produto();
        p1.setDescricaoProduto("Mouse Gamer");
        p1.setEstoqueAtual(2);
        gerenciador.cadastrarNovoProduto(p1);

        Exception exception = assertThrows(Exception.class, () ->
            gerenciador.baixarEstoque(1, 5)
        );

        assertTrue(exception.getMessage().contains("Estoque insuficiente"),
                "A mensagem de erro deveria alertar sobre o estoque insuficiente.");
    }
}