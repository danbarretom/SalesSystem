public abstract class Venda implements Imprimivel {
    protected int numeroVenda;
    protected String dataVenda;

    public Venda() {}

    public Venda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getNumeroVenda() { return numeroVenda; }
    public void setNumeroVenda(int numeroVenda) { this.numeroVenda = numeroVenda; }
    public String getDataVenda() { return dataVenda; }
    public void setDataVenda(String dataVenda) { this.dataVenda = dataVenda; }

    public abstract String getTipoVenda();
    public abstract int getCodigoCliente();
    public abstract String getDataVencimento();
    public abstract String gerarLinhaArquivo();
}