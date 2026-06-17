package Modelos;
import Interfaces.Imprimivel;

public class Produto implements Imprimivel {
    private int codigoProduto;
    private String descricaoProduto;
    private double valorCompra;
    private double valorVenda;
    private int estoqueAtual;
    private int estoqueMinimo;

    public Produto() {}

    public Produto(int codigoProduto, String descricaoProduto, double valorCompra, double valorVenda, int estoqueAtual, int estoqueMinimo) {
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(int codigoProduto) { this.codigoProduto = codigoProduto; }
    public String getDescricaoProduto() { return descricaoProduto; }
    public void setDescricaoProduto(String descricaoProduto) { this.descricaoProduto = descricaoProduto; }
    public double getValorCompra() { return valorCompra; }
    public void setValorCompra(double valorCompra) { this.valorCompra = valorCompra; }
    public double getValorVenda() { return valorVenda; }
    public void setValorVenda(double valorVenda) { this.valorVenda = valorVenda; }
    public int getEstoqueAtual() { return estoqueAtual; }
    public void setEstoqueAtual(int quantidadeEstoque) { this.estoqueAtual = quantidadeEstoque; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

    @Override
    public void exibirDetalhes() {
        System.out.println("--- DADOS DO PRODUTO ---");
        System.out.println("Código: " + this.codigoProduto);
        System.out.println("Descrição: " + this.descricaoProduto);
        System.out.printf("Valor de Compra: R$ %.2f\n", this.valorCompra);
        System.out.printf("Valor de BasicClasses.Venda: R$ %.2f\n", this.valorVenda);
        System.out.println("Estoque Atual: " + this.estoqueAtual);
        System.out.println("Estoque Mínimo: " + this.estoqueMinimo);
        System.out.println("------------------------");
    }
}