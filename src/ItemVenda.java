public class ItemVenda implements Imprimivel {
    private int numeroVenda;
    private int codigoProduto;
    private int quantidadeVendida;
    private double valorVenda;

    public ItemVenda() {}

    public ItemVenda(int numeroVenda, int codigoProduto, int quantidadeVendida, double valorVenda) {
        this.numeroVenda = numeroVenda;
        this.codigoProduto = codigoProduto;
        this.quantidadeVendida = quantidadeVendida;
        this.valorVenda = valorVenda;
    }

    public int getNumeroVenda() { return numeroVenda; }
    public void setNumeroVenda(int numeroVenda) { this.numeroVenda = numeroVenda; }
    public int getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(int codigoProduto) { this.codigoProduto = codigoProduto; }
    public int getQuantidadeVendida() { return quantidadeVendida; }
    public void setQuantidadeVendida(int quantidadeVendida) { this.quantidadeVendida = quantidadeVendida; }
    public double getValorVenda() { return valorVenda; }
    public void setValorVenda(double valorVenda) { this.valorVenda = valorVenda; }

    @Override
    public void exibirDetalhes() {
        System.out.println("--- ITEM DA VENDA ---");
        System.out.println("Número da Venda: " + this.numeroVenda);
        System.out.println("Código do Produto: " + this.codigoProduto);
        System.out.println("Quantidade: " + this.quantidadeVendida);
        System.out.println("Valor: R$ " + this.valorVenda);
        System.out.println("---------------------");
    }
}