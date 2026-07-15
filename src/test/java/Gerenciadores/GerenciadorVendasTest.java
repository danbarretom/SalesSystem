package Gerenciadores;

import Modelos.VendaVista;
import Modelos.VendaPrazo;
import Modelos.ItemVenda;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GerenciadorVendasTest {

    private GerenciadorVendas gerenciador;
    private final String ARQUIVO_VENDAS_TESTE = "vendas_teste.txt";
    private final String ARQUIVO_ITENS_TESTE = "itens_teste.txt";

    @BeforeEach
    void setUp() {
        // Inicializa o gerenciador com os dois arquivos de teste isolados
        gerenciador = new GerenciadorVendas(ARQUIVO_VENDAS_TESTE, ARQUIVO_ITENS_TESTE);
    }

    @AfterEach
    void tearDown() {
        // Deleta o arquivo de vendas de teste tratando o resultado para evitar warnings
        File arqVendas = new File(ARQUIVO_VENDAS_TESTE);
        if (arqVendas.exists()) {
            boolean deletadoVendas = arqVendas.delete();
            if (!deletadoVendas) {
                System.err.println("Aviso: Não foi possível apagar o arquivo de vendas de teste.");
            }
        }

        // Deleta o arquivo de itens de teste tratando o resultado para evitar warnings
        File arqItens = new File(ARQUIVO_ITENS_TESTE);
        if (arqItens.exists()) {
            boolean deletadoItens = arqItens.delete();
            if (!deletadoItens) {
                System.err.println("Aviso: Não foi possível apagar o arquivo de itens de teste.");
            }
        }
    }

    @Test
    void deveGerarNumeroDeVendaSequencial() {
        // Como o banco de testes inicia vazio, o primeiro ID gerado deve ser 1
        assertEquals(1, gerenciador.gerarNumeroVenda(), "A primeira venda registrada deve possuir o número 1.");

        // Registra uma venda à vista para ocupar o ID 1
        VendaVista venda1 = new VendaVista("14/07/2026");
        gerenciador.realizarVenda(venda1);

        // O próximo número gerado obrigatoriamente deve ser o 2
        assertEquals(2, gerenciador.gerarNumeroVenda(), "A segunda venda registrada deve possuir o número 2.");
    }

    @Test
    void deveRealizarVendaERegistrarDataAtualAutomaticamente() {
        // Criamos uma venda com data vazia para forçar o acionamento do método capturarData()
        VendaVista novaVenda = new VendaVista("");

        gerenciador.realizarVenda(novaVenda);

        assertEquals(1, gerenciador.lista.size(), "A lista deveria conter exatamente 1 venda adicionada.");
        assertNotNull(novaVenda.getDataVenda(), "A data da venda não deveria ser nula.");
        assertFalse(novaVenda.getDataVenda().isEmpty(), "O sistema deveria preencher a data da venda de forma automática.");
    }

    @Test
    void deveRegistrarItemDaVendaEVerificarSeProdutoFoiVendido() {
        // Registramos um item associado à venda de número 1, referente ao produto de código 99
        ItemVenda item = new ItemVenda(1, 99, 5, 45.0);
        gerenciador.registrarItemVenda(item);

        // Valida se o sistema reconhece que o produto 99 está associado a uma venda
        assertTrue(gerenciador.verificarProdutoEmVenda(99), "O método deveria retornar true para um produto que foi vendido.");
        assertFalse(gerenciador.verificarProdutoEmVenda(100), "O método deveria retornar false para um produto que nunca foi vendido.");
    }

    @Test
    void deveVerificarSeClientePossuiVendaRegistrada() {
        // Registra uma venda a prazo para o cliente de código 5
        VendaPrazo vendaPrazo = new VendaPrazo("14/07/2026", 5, "30/07/2026");
        gerenciador.realizarVenda(vendaPrazo);

        // Valida se o sistema reconhece que o cliente 5 possui um vínculo com vendas registradas
        assertTrue(gerenciador.verificarClienteEmVenda(5), "O método deveria retornar true para o cliente com venda a prazo.");
        assertFalse(gerenciador.verificarClienteEmVenda(10), "O método deveria retornar false para um cliente sem vendas registradas.");
    }
}
