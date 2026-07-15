package Gerenciadores;

import Modelos.Produto;
import Excecoes.EntidadeNaoEncontradaException;
import Excecoes.RegraNegocioException;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorProdutos extends GerenciadorBase<Produto> {

    public GerenciadorProdutos() {
        super("produtos.txt");
    }

    public GerenciadorProdutos(String arquivoTeste) {
        super(arquivoTeste);
    }

    @Override
    protected Produto criarObjetoDaLinha(String linha) {
        String[] dados = linha.split(";");
        Produto p = new Produto();
        p.setCodigoProduto(Integer.parseInt(dados[0]));
        p.setDescricaoProduto(dados[1]);
        p.setValorCompra(Double.parseDouble(dados[2]));
        p.setValorVenda(Double.parseDouble(dados[3]));
        p.setEstoqueAtual(Integer.parseInt(dados[4]));
        p.setEstoqueMinimo(Integer.parseInt(dados[5]));
        return p;
    }

    @Override
    protected String gerarLinhaDoObjeto(Produto p) {
        return p.getCodigoProduto() + ";" + p.getDescricaoProduto() + ";" +
                p.getValorCompra() + ";" + p.getValorVenda() + ";" +
                p.getEstoqueAtual() + ";" + p.getEstoqueMinimo();
    }

    public int cadastrarNovoProduto(Produto produto) {
        int novoCodigo = 1;
        if (!lista.isEmpty()) {
            int maiorCodigo = 0;
            for (Produto p : lista) {
                if (p.getCodigoProduto() > maiorCodigo) maiorCodigo = p.getCodigoProduto();
            }
            novoCodigo = maiorCodigo + 1;
        }
        produto.setCodigoProduto(novoCodigo);
        lista.add(produto);
        salvarDados();
        return novoCodigo;
    }

    public Produto consultarProduto(int codigo) {
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigo) {
                return p;
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe produto com o código " + codigo);
    }

    public void excluirProduto(int codigo) {
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigo) {
                lista.remove(p);
                salvarDados();
                return;
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe produto com o código " + codigo);
    }

    public void alterarProduto(int codigo, Produto produtoAlterado) {
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigo) {
                p.setDescricaoProduto(produtoAlterado.getDescricaoProduto());
                p.setValorCompra(produtoAlterado.getValorCompra());
                p.setValorVenda(produtoAlterado.getValorVenda());
                p.setEstoqueAtual(produtoAlterado.getEstoqueAtual());
                p.setEstoqueMinimo(produtoAlterado.getEstoqueMinimo());
                salvarDados();
                return;
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe produto com o código " + codigo);
    }

    public void baixarEstoque(int codigoProduto, int quantidade) {
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigoProduto) {
                if (p.getEstoqueAtual() >= quantidade) {
                    p.setEstoqueAtual(p.getEstoqueAtual() - quantidade);
                    salvarDados();
                    return;
                } else {
                    throw new RegraNegocioException("Estoque insuficiente! Quantidade atual disponível: " + p.getEstoqueAtual());
                }
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe produto com o código " + codigoProduto);
    }

    public Produto buscarProduto(int codigoProduto) {
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigoProduto) {
                return p;
            }
        }
        throw new EntidadeNaoEncontradaException("Não existe produto com o código " + codigoProduto);
    }

    public List<Produto> consultarEstoqueBaixo() {
        List<Produto> produtosComEstoqueBaixo = new ArrayList<>();
        for (Produto p : lista) {
            if (p.getEstoqueAtual() < p.getEstoqueMinimo()) {
                produtosComEstoqueBaixo.add(p);
            }
        }
        return produtosComEstoqueBaixo;
    }
}