package Gerenciadores;

import Modelos.Cliente;
import Excecoes.EntidadeNaoEncontradaException; // Importando o nosso erro

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


    public void consultarCliente(int codigo) {
        for (Cliente c : lista) {
            if (c.getCodigoCliente() == codigo) {
                c.exibirDetalhes();
                return;
            }
        }

        throw new EntidadeNaoEncontradaException("Não existe cliente com o código " + codigo);
    }

    public void excluirCliente(int codigo) {
        for (Cliente c : lista) {
            if (c.getCodigoCliente() == codigo) {
                lista.remove(c);
                salvarDados();
                System.out.println("Cliente com o código " + codigo + " removido com sucesso.");
                return;
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe cliente com o código " + codigo);
    }

    public void alterarCliente(int codigo, Cliente clienteAlterado) {
        for (Cliente c : lista) {
            if (c.getCodigoCliente() == codigo) {
                c.setNomeCliente(clienteAlterado.getNomeCliente());
                c.setEnderecoCliente(clienteAlterado.getEnderecoCliente());
                c.setTelefoneCliente(clienteAlterado.getTelefoneCliente());
                salvarDados();
                System.out.println("Cliente com o código " + codigo + " alterado com sucesso.");
                return;
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe cliente com o código " + codigo);
    }
}