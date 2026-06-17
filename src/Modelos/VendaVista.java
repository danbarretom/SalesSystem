package Modelos;

public class VendaVista extends Venda {

    public VendaVista() {}

    public VendaVista(String dataVenda) {
        super(dataVenda);
    }

    @Override
    public String getTipoVenda() { return "VISTA"; }

    @Override
    public int getCodigoCliente() { return -1; }

    @Override
    public String getDataVencimento() { return getDataVenda(); }

    @Override
    public String gerarLinhaArquivo() {
        return getNumeroVenda() + ";" + getDataVenda() + ";VISTA;-1;" + getDataVenda();
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("--- VENDA À VISTA ---");
        System.out.println("Número: " + getNumeroVenda());
        System.out.println("Data da BasicClasses.Venda: " + getDataVenda());
        System.out.println("---------------------");
    }
}