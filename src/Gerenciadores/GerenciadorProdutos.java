package Gerenciadores;
import BasicClasses.Produto;

public class GerenciadorProdutos extends GerenciadorBase<Produto> {

    public GerenciadorProdutos() {
        super("produtos.txt");
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

    public void cadastrarNovoProduto(Produto produto) {
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
        System.out.println("Produto cadastrado com o código: " + novoCodigo);
    }

    public void consultarProduto(int codigo) throws Exception {
        boolean codigoExiste = false;
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigo) {
                p.exibirDetalhes();
                codigoExiste = true;
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe produto com esse código.");
    }

    public void excluirProduto(int codigo) throws Exception {
        boolean codigoExiste = false;
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigo) {
                lista.remove(p);
                salvarDados();
                codigoExiste = true;
                System.out.println("Produto com o código " + codigo + " removido com sucesso.");
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe produto com esse código.");
    }

    public void alterarProduto(int codigo, Produto produtoAlterado) throws Exception {
        boolean codigoExiste = false;
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigo) {
                p.setDescricaoProduto(produtoAlterado.getDescricaoProduto());
                p.setValorCompra(produtoAlterado.getValorCompra());
                p.setValorVenda(produtoAlterado.getValorVenda());
                p.setEstoqueAtual(produtoAlterado.getEstoqueAtual());
                p.setEstoqueMinimo(produtoAlterado.getEstoqueMinimo());
                salvarDados();
                codigoExiste = true;
                System.out.println("Produto com o código " + codigo + " alterado com sucesso.");
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe produto com esse código.");
    }

    public void baixarEstoque(int codigoProduto, int quantidade) throws Exception {
        boolean codigoExiste = false;
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigoProduto) {
                codigoExiste = true;
                if (p.getEstoqueAtual() >= quantidade) {
                    p.setEstoqueAtual(p.getEstoqueAtual() - quantidade);
                    salvarDados();
                } else {
                    throw new Exception("Estoque insuficiente! Quantidade atual disponível: " + p.getEstoqueAtual());
                }
                break;
            }
        }
        if (!codigoExiste) throw new Exception("Não existe produto com esse código.");
    }

    public Produto buscarProduto(int codigoProduto) throws Exception {
        for (Produto p : lista) {
            if (p.getCodigoProduto() == codigoProduto) {
                return p;
            }
        }
        throw new Exception("Não existe produto com esse código.");
    }

    public void consultarEstoqueBaixo() {
        System.out.println("\n==================================================");
        System.out.println("       PRODUTOS COM ESTOQUE ABAIXO DO MÍNIMO      ");
        System.out.println("==================================================");
        System.out.println("CÓDIGO | DESCRIÇÃO | ATUAL | MÍNIMO");
        System.out.println("--------------------------------------------------");
        int contador = 0;
        for (Produto p : lista) {
            if (p.getEstoqueAtual() < p.getEstoqueMinimo()) {
                System.out.println(p.getCodigoProduto() + " | " +
                        p.getDescricaoProduto() + " | " +
                        p.getEstoqueAtual() + " | " +
                        p.getEstoqueMinimo());
                contador++;
            }
        }
        if (contador == 0) System.out.println("Excelente! Todos os produtos estão com estoque regular.");
    }
}