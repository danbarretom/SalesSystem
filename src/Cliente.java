public class Cliente implements Imprimivel {
    private int codigoCliente;
    private String nomeCliente;
    private String enderecoCliente;
    private String telefoneCliente;

    public Cliente() {}

    public Cliente(int codigoCliente, String nomeCliente, String enderecoCliente, String telefoneCliente) {
        this.codigoCliente = codigoCliente;
        this.nomeCliente = nomeCliente;
        this.enderecoCliente = enderecoCliente;
        this.telefoneCliente = telefoneCliente;
    }

    public int getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(int codigoCliente) { this.codigoCliente = codigoCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getEnderecoCliente() { return enderecoCliente; }
    public void setEnderecoCliente(String enderecoCliente) { this.enderecoCliente = enderecoCliente; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public void setTelefoneCliente(String telefoneCliente) { this.telefoneCliente = telefoneCliente; }

    @Override
    public void exibirDetalhes() {
        System.out.println("--- DADOS DO CLIENTE ---");
        System.out.println("Código: " + this.codigoCliente);
        System.out.println("Nome: " + this.nomeCliente);
        System.out.println("Endereço: " + this.enderecoCliente);
        System.out.println("Telefone: " + this.telefoneCliente);
        System.out.println("------------------------");
    }
}