package Gerenciadores;
import BasicClasses.Venda;
import BasicClasses.VendaPrazo;
import BasicClasses.VendaVista;
import BasicClasses.ItemVenda;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class GerenciadorVendas extends GerenciadorBase<Venda> {
    private ArrayList<ItemVenda> listaItemVendas;
    private final String ARQUIVO_ITENS = "itens_vendas.txt";

    public GerenciadorVendas() {
        super("vendas.txt");
        this.listaItemVendas = new ArrayList<>();
        carregarItens();
    }

    @Override
    protected Venda criarObjetoDaLinha(String linha) {
        String[] dados = linha.split(";");
        Venda v;
        if (dados[2].equals("VISTA")) {
            v = new VendaVista(dados[1]);
        } else {
            v = new VendaPrazo(dados[1], Integer.parseInt(dados[3]), dados[4]);
        }
        v.setNumeroVenda(Integer.parseInt(dados[0]));
        return v;
    }

    @Override
    protected String gerarLinhaDoObjeto(Venda v) {
        return v.gerarLinhaArquivo();
    }

    private void carregarItens() {
        try {
            File arquivo = new File(ARQUIVO_ITENS);
            if (!arquivo.exists()) return;

            Scanner leitor = new Scanner(arquivo);
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";");
                ItemVenda i = new ItemVenda();
                i.setNumeroVenda(Integer.parseInt(dados[0]));
                i.setCodigoProduto(Integer.parseInt(dados[1]));
                i.setQuantidadeVendida(Integer.parseInt(dados[2]));
                i.setValorVenda(Double.parseDouble(dados[3]));
                listaItemVendas.add(i);
            }
            leitor.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo itens_vendas.txt: " + e.getMessage());
        }
    }

    private void salvarItensVendas() {
        try {
            FileWriter escritor = new FileWriter(ARQUIVO_ITENS);
            for (ItemVenda i : listaItemVendas) {
                String linha = i.getNumeroVenda() + ";" + i.getCodigoProduto() + ";" +
                        i.getQuantidadeVendida() + ";" + i.getValorVenda();
                escritor.write(linha + "\n");
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo itens_vendas.txt: " + e.getMessage());
        }
    }

    public boolean verificarProdutoEmVenda(int codigoProduto) {
        for (ItemVenda item : listaItemVendas) {
            if (item.getCodigoProduto() == codigoProduto) return true;
        }
        return false;
    }

    public boolean verificarClienteEmVenda(int codigoCliente) {
        for (Venda v : lista) {
            if (v.getCodigoCliente() == codigoCliente) return true;
        }
        return false;
    }

    public int gerarNumeroVenda() {
        int novoNumero = 1;
        if (!lista.isEmpty()) {
            int maiorNumero = 0;
            for (Venda v : lista) {
                if (v.getNumeroVenda() > maiorNumero) maiorNumero = v.getNumeroVenda();
            }
            novoNumero = maiorNumero + 1;
        }
        return novoNumero;
    }

    public String capturarData() {
        LocalDate hoje = LocalDate.now();
        return String.format("%02d/%02d/%d", hoje.getDayOfMonth(), hoje.getMonthValue(), hoje.getYear());
    }

    public void realizarVenda(Venda venda) {
        venda.setNumeroVenda(gerarNumeroVenda());
        if (venda.getDataVenda() == null || venda.getDataVenda().isEmpty()) {
            venda.setDataVenda(capturarData());
        }
        lista.add(venda);
        salvarDados();
        System.out.println("Venda número " + venda.getNumeroVenda() + " registrada com sucesso!");
    }

    public void registrarItemVenda(ItemVenda item) {
        listaItemVendas.add(item);
        salvarItensVendas();
    }

    public void consultarVendasPorPeriodo(String dataIniStr, String dataFimStr) {
        System.out.println("\n==================================================");
        System.out.println("               RELATÓRIO DE VENDAS                ");
        System.out.println("==================================================");
        System.out.println("Nº VENDA | DATA | TIPO | CLIENTE | VENCIMENTO");
        System.out.println("--------------------------------------------------");

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicial = LocalDate.parse(dataIniStr, formato);
        LocalDate dataFinal = LocalDate.parse(dataFimStr, formato);

        int contadorVendas = 0;
        for (Venda v : lista) {
            LocalDate dataVendaAtual = LocalDate.parse(v.getDataVenda(), formato);
            if ((dataVendaAtual.isEqual(dataInicial) || dataVendaAtual.isAfter(dataInicial)) &&
                    (dataVendaAtual.isEqual(dataFinal) || dataVendaAtual.isBefore(dataFinal))) {

                System.out.println(v.getNumeroVenda() + " | " + v.getDataVenda() + " | " +
                        v.getTipoVenda() + " | " +
                        (v.getCodigoCliente() == -1 ? "N/A" : v.getCodigoCliente()) + " | " +
                        v.getDataVencimento());
                contadorVendas++;
            }
        }
        if (contadorVendas == 0) {
            System.out.println("Nenhuma venda encontrada no período selecionado.");
        } else {
            System.out.println("--------------------------------------------------");
            System.out.println("Total de vendas no período: " + contadorVendas);
        }
    }
}