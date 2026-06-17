public class VendaPrazo extends Venda {
    private int codigoCliente;
    private String dataVencimento;

    public VendaPrazo() {}

    public VendaPrazo(String dataVenda, int codigoCliente, String dataVencimento) {
        super(dataVenda);
        this.codigoCliente = codigoCliente;
        this.dataVencimento = dataVencimento;
    }

    @Override
    public String getTipoVenda() { return "PRAZO"; }

    @Override
    public int getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(int codigoCliente) { this.codigoCliente = codigoCliente; }

    @Override
    public String getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(String dataVencimento) { this.dataVencimento = dataVencimento; }

    @Override
    public String gerarLinhaArquivo() {
        return getNumeroVenda() + ";" + getDataVenda() + ";PRAZO;" + getCodigoCliente() + ";" + getDataVencimento();
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("--- VENDA A PRAZO ---");
        System.out.println("Número: " + getNumeroVenda());
        System.out.println("Data da Venda: " + getDataVenda());
        System.out.println("Código do Cliente: " + getCodigoCliente());
        System.out.println("Data de Vencimento: " + getDataVencimento());
        System.out.println("---------------------");
    }
}