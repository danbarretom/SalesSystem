package Gerenciadores;

import Modelos.Produto;
import Excecoes.EntidadeNaoEncontradaException;
import Excecoes.RegraNegocioException;
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

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () ->
            gerenciador.baixarEstoque(1, 5)
        );

        assertTrue(exception.getMessage().contains("Estoque insuficiente"),
                "A mensagem de erro deveria alertar sobre o estoque insuficiente.");
    }

    @Test
    void devePermitirBaixarEstoqueQuandoQuantidadeIgualAoEstoqueAtual() {
        Produto p1 = new Produto();
        p1.setDescricaoProduto("Webcam HD");
        p1.setEstoqueAtual(4);
        gerenciador.cadastrarNovoProduto(p1);

        gerenciador.baixarEstoque(1, 4);

        Produto atualizado = gerenciador.buscarProduto(1);
        assertEquals(0, atualizado.getEstoqueAtual(),
                "Quando a quantidade vendida é igual ao estoque atual, o estoque deve zerar sem lançar exceção.");
    }

    @Test
    void deveGerarLinhaEReconstruirProdutoCorretamente() {
        Produto original = new Produto();
        original.setCodigoProduto(7);
        original.setDescricaoProduto("Cadeira Gamer");
        original.setValorCompra(450.5);
        original.setValorVenda(699.9);
        original.setEstoqueAtual(12);
        original.setEstoqueMinimo(3);

        String linha = gerenciador.gerarLinhaDoObjeto(original);
        Produto reconstruido = gerenciador.criarObjetoDaLinha(linha);

        assertEquals(original.getCodigoProduto(), reconstruido.getCodigoProduto());
        assertEquals(original.getDescricaoProduto(), reconstruido.getDescricaoProduto());
        assertEquals(original.getValorCompra(), reconstruido.getValorCompra());
        assertEquals(original.getValorVenda(), reconstruido.getValorVenda());
        assertEquals(original.getEstoqueAtual(), reconstruido.getEstoqueAtual());
        assertEquals(original.getEstoqueMinimo(), reconstruido.getEstoqueMinimo());
    }

    @Test
    void deveLancarExcecaoAoConsultarProdutoInexistente() {
        assertThrows(EntidadeNaoEncontradaException.class, () ->
            gerenciador.consultarProduto(999)
        );
    }

    @Test
    void deveLancarExcecaoAoExcluirProdutoInexistente() {
        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () ->
            gerenciador.excluirProduto(999)
        );
        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void deveLancarExcecaoAoAlterarProdutoInexistente() {
        Produto produtoAlterado = new Produto();
        produtoAlterado.setDescricaoProduto("Não importa");

        assertThrows(EntidadeNaoEncontradaException.class, () ->
            gerenciador.alterarProduto(999, produtoAlterado)
        );
    }
}