package Gerenciadores;
import BasicClasses.Cliente;

public class GerenciadorClientes extends GerenciadorBase<Cliente> {

    public GerenciadorClientes() {
        super("clientes.txt");
    }

    @Override
    protected Cliente criarObjetoDaLinha(String linha) {
        String[] dados = linha.split(";");
        Cliente c = new Cliente();
        c.setCodigoCliente(Integer.parseInt(dados[0]));
        c.setNomeCliente(dados[1]);
        c.setEnderecoCliente(dados[2]);
        c.setTelefoneCliente(dados[3]);
        return c;
    }

    @Override
    protected String gerarLinhaDoObjeto(Cliente c) {
        return c.getCodigoCliente() + ";" + c.getNomeCliente() + ";" +
                c.getEnderecoCliente() + ";" + c.getTelefoneCliente();
    }

    public void cadastrarNovoCliente(Cliente cliente) {
        int novoCodigo = 1;
        if (!lista.isEmpty()) {
            int maiorCodigo = 0;
            for (Cliente c : lista) {
                if (c.getCodigoCliente() > maiorCodigo) maiorCodigo = c.getCodigoCliente();
            }
            novoCodigo = maiorCodigo + 1;
        }
        cliente.setCodigoCliente(novoCodigo);
        lista.add(cliente);
        salvarDados();
        System.out.println("Cliente cadastrado com o código: " + novoCodigo);
    }

    public void consultarCliente(int codigo) throws Exception {
        boolean codigoExiste = false;
        for (Cliente c : lista) {
            if (c.getCodigoCliente() == codigo) {
                c.exibirDetalhes();
                codigoExiste = true;
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe cliente com esse código.");
    }

    public void excluirCliente(int codigo) throws Exception {
        boolean codigoExiste = false;
        for (Cliente c : lista) {
            if (c.getCodigoCliente() == codigo) {
                lista.remove(c);
                salvarDados();
                codigoExiste = true;
                System.out.println("Cliente com o código " + codigo + " removido com sucesso.");
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe cliente com esse código.");
    }

    public void alterarCliente(int codigo, Cliente clienteAlterado) throws Exception {
        boolean codigoExiste = false;
        for (Cliente c : lista) {
            if (c.getCodigoCliente() == codigo) {
                c.setNomeCliente(clienteAlterado.getNomeCliente());
                c.setEnderecoCliente(clienteAlterado.getEnderecoCliente());
                c.setTelefoneCliente(clienteAlterado.getTelefoneCliente());
                salvarDados();
                codigoExiste = true;
                System.out.println("Cliente com o código " + codigo + " alterado com sucesso.");
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe cliente com esse código.");
    }
}