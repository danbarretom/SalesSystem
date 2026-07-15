import Gerenciadores.*;
import Modelos.*;
import Excecoes.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ViewSistema {
    private Scanner scanner;
    private GerenciadorProdutos gerenciadorProdutos;
    private GerenciadorVendas gerenciadorVendas;
    private GerenciadorClientes gerenciadorClientes;

    public ViewSistema() {
        this.scanner = new Scanner(System.in);
        this.gerenciadorProdutos = new GerenciadorProdutos();
        this.gerenciadorVendas = new GerenciadorVendas();
        this.gerenciadorClientes = new GerenciadorClientes();
    }

    public void iniciar() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=====================");
            System.out.println("    MENU INICIAL     ");
            System.out.println("=====================");
            System.out.println("1. Manutenção no Arquivo de Produto");
            System.out.println("2. Manutenção no Arquivo de Cliente");
            System.out.println("3. Manutenção no Arquivo de Vendas");
            System.out.println("4. Realizar Consultas");
            System.out.println("0. Encerrar a Execução da Aplicação");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nPor favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    menuProdutos();
                    break;
                case 2:
                    menuClientes();
                    break;
                case 3:
                    menuVendas();
                    break;
                case 4:
                    menuConsultas();
                    break;
                case 0:
                    System.out.println("\nEncerrando o programa...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
                    break;
            }
        }
        scanner.close();
    }

    private void menuProdutos() {
        int opcaop = -1;
        while (opcaop != 0) {
            System.out.println("\n=======================================");
            System.out.println("    MENU DE MANUTENÇÃO DE PRODUTOS     ");
            System.out.println("=======================================");
            System.out.println("1. Cadastrar Novo Produto");
            System.out.println("2. Consultar Produto");
            System.out.println("3. Alterar Produto");
            System.out.println("4. Excluir Produto");
            System.out.println("0. Voltar ao Menu Anterior");
            System.out.print("Escolha uma opção: ");

            try {
                opcaop = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
                continue;
            }

            switch (opcaop) {
                case 1:
                    System.out.println("\n--- CADASTRAR NOVO PRODUTO ---");
                    Produto novoProduto = new Produto();
                    System.out.print("Descrição do produto: ");
                    novoProduto.setDescricaoProduto(scanner.nextLine());
                    System.out.print("Valor de compra: R$ ");
                    novoProduto.setValorCompra(Double.parseDouble(scanner.nextLine()));
                    System.out.print("Valor de venda: R$ ");
                    novoProduto.setValorVenda(Double.parseDouble(scanner.nextLine()));
                    System.out.print("Estoque atual: ");
                    novoProduto.setEstoqueAtual(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Estoque mínimo: ");
                    novoProduto.setEstoqueMinimo(Integer.parseInt(scanner.nextLine()));

                    int codigoGerado = gerenciadorProdutos.cadastrarNovoProduto(novoProduto);
                    System.out.println("Produto cadastrado com o código: " + codigoGerado);
                    break;

                case 2:
                    System.out.println("\n--- CONSULTAR PRODUTO ---");
                    System.out.print("Digite o código do produto: ");
                    try {
                        int codigoConsulta = Integer.parseInt(scanner.nextLine());
                        Produto produtoConsultado = gerenciadorProdutos.consultarProduto(codigoConsulta);
                        produtoConsultado.exibirDetalhes();
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\n--- ALTERAR PRODUTO ---");
                    System.out.print("Digite o código do produto que deseja alterar: ");
                    try {
                        int codigoAlterar = Integer.parseInt(scanner.nextLine());
                        Produto produtoAtual = gerenciadorProdutos.consultarProduto(codigoAlterar);
                        produtoAtual.exibirDetalhes();

                        System.out.println("\nDigite os NOVOS dados do produto:");
                        Produto produtoAlterado = new Produto();

                        System.out.print("Nova descrição: ");
                        produtoAlterado.setDescricaoProduto(scanner.nextLine());
                        System.out.print("Novo valor de compra: R$ ");
                        produtoAlterado.setValorCompra(Double.parseDouble(scanner.nextLine()));
                        System.out.print("Novo valor de venda: R$ ");
                        produtoAlterado.setValorVenda(Double.parseDouble(scanner.nextLine()));
                        System.out.print("Novo estoque atual: ");
                        produtoAlterado.setEstoqueAtual(Integer.parseInt(scanner.nextLine()));
                        System.out.print("Novo estoque mínimo: ");
                        produtoAlterado.setEstoqueMinimo(Integer.parseInt(scanner.nextLine()));

                        gerenciadorProdutos.alterarProduto(codigoAlterar, produtoAlterado);
                        System.out.println("Produto com o código " + codigoAlterar + " alterado com sucesso.");

                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("\n--- EXCLUIR PRODUTO ---");
                    System.out.print("Digite o código do produto que deseja excluir: ");
                    try {
                        int codigoExcluir = Integer.parseInt(scanner.nextLine());
                        if (gerenciadorVendas.verificarProdutoEmVenda(codigoExcluir)) {
                            System.out.println("Erro: Este produto não pode ser excluído pois já está atrelado a uma venda registrada.");
                        } else {
                            gerenciadorProdutos.excluirProduto(codigoExcluir);
                            System.out.println("Produto com o código " + codigoExcluir + " removido com sucesso.");
                        }
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;

                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
                    break;
            }
        }
    }

    private void menuClientes() {
        int opcaoc = -1;
        while (opcaoc != 0) {
            System.out.println("\n======================================");
            System.out.println("    MENU DE MANUTENÇÃO DE CLIENTE     ");
            System.out.println("======================================");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Consultar Cliente");
            System.out.println("3. Alterar Cliente");
            System.out.println("4. Excluir Cliente");
            System.out.println("0. Voltar ao Menu Anterior");
            System.out.print("Escolha uma opção: ");

            try {
                opcaoc = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
                continue;
            }

            switch (opcaoc) {
                case 1:
                    System.out.println("\n--- CADASTRAR NOVO CLIENTE ---");
                    Cliente novoCliente = new Cliente();
                    System.out.print("Nome do Cliente: ");
                    novoCliente.setNomeCliente(scanner.nextLine());
                    System.out.print("Endereço do Cliente: ");
                    novoCliente.setEnderecoCliente(scanner.nextLine());
                    System.out.print("Telefone do Cliente: ");
                    novoCliente.setTelefoneCliente(scanner.nextLine());
                    int codigoGeradoCliente = gerenciadorClientes.cadastrarNovoCliente(novoCliente);
                    System.out.println("Cliente cadastrado com o código: " + codigoGeradoCliente);
                    break;

                case 2:
                    System.out.println("\n--- CONSULTAR CLIENTE ---");
                    System.out.print("Digite o código do cliente: ");
                    try {
                        int codigoConsulta = Integer.parseInt(scanner.nextLine());
                        Cliente clienteConsultado = gerenciadorClientes.consultarCliente(codigoConsulta);
                        clienteConsultado.exibirDetalhes();
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\n--- ALTERAR CLIENTE ---");
                    System.out.print("Digite o código do cliente que deseja alterar: ");
                    try {
                        int codigoAlterar = Integer.parseInt(scanner.nextLine());
                        Cliente clienteAtual = gerenciadorClientes.consultarCliente(codigoAlterar);
                        System.out.println("Cliente atual:");
                        clienteAtual.exibirDetalhes();

                        System.out.println("\nDigite os NOVOS dados do cliente:");
                        Cliente clienteAlterado = new Cliente();
                        System.out.print("Novo nome: ");
                        clienteAlterado.setNomeCliente(scanner.nextLine());
                        System.out.print("Novo Endereço: ");
                        clienteAlterado.setEnderecoCliente(scanner.nextLine());
                        System.out.print("Novo telefone: ");
                        clienteAlterado.setTelefoneCliente(scanner.nextLine());

                        gerenciadorClientes.alterarCliente(codigoAlterar, clienteAlterado);
                        System.out.println("Cliente com o código " + codigoAlterar + " alterado com sucesso.");
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("\n--- EXCLUIR CLIENTE ---");
                    System.out.print("Digite o código do cliente que deseja excluir: ");
                    try {
                        int codigoExcluir = Integer.parseInt(scanner.nextLine());
                        if (gerenciadorVendas.verificarClienteEmVenda(codigoExcluir)) {
                            System.out.println("Erro: Este cliente não pode ser excluído pois possui vendas a prazo registradas.");
                        } else {
                            gerenciadorClientes.excluirCliente(codigoExcluir);
                            System.out.println("Cliente com o código " + codigoExcluir + " removido com sucesso.");
                        }
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;

                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
                    break;
            }
        }
    }

    private void menuVendas() {
        System.out.println("\n======================================");
        System.out.println("          REALIZAR NOVA VENDA         ");
        System.out.println("======================================");

        String tipoVenda = "";
        boolean tipoValido = false;

        while (!tipoValido) {
            System.out.print("Qual o tipo da venda? (VISTA/PRAZO ou 0 para voltar ao menu anterior) : ");
            tipoVenda = scanner.nextLine().trim().toUpperCase();

            if (tipoVenda.equals("0")) {
                System.out.println("Operação cancelada. Voltando ao menu...\n");
                return;
            } else if (tipoVenda.equals("VISTA") || tipoVenda.equals("PRAZO")) {
                tipoValido = true;
            } else {
                System.out.println("Erro: Tipo de venda inválido. Por favor, digite exatamente VISTA ou PRAZO.\n");
            }
        }

        String dataHoje = gerenciadorVendas.capturarData();
        Venda novaVenda;

        if (tipoVenda.equals("PRAZO")) {
            int codigoCliente = -1;
            boolean clienteValido = false;

            while (!clienteValido) {
                try {
                    System.out.print("Entre com o código do cliente: ");
                    codigoCliente = Integer.parseInt(scanner.nextLine());
                    gerenciadorClientes.consultarCliente(codigoCliente);
                    clienteValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("Erro: Formato inválido. O código do cliente deve conter apenas números.\n");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println("Erro: " + e.getMessage() + "\n");
                }
            }

            String dataVencimento = "";
            boolean dataValida = false;
            while (!dataValida) {
                try {
                    System.out.print("Entre com a data de vencimento (DD/MM/AAAA): ");
                    dataVencimento = scanner.nextLine();

                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataVencParsed = LocalDate.parse(dataVencimento, formato);
                    LocalDate dataAtual = LocalDate.now();

                    if (dataVencParsed.isBefore(dataAtual)) {
                        System.out.println("Erro: A data de vencimento não pode ser anterior à data de hoje (" + dataAtual.format(formato) + ").\n");
                    } else {
                        dataValida = true;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Erro: Formato de data inválido ou data inexistente. Use o padrão DD/MM/AAAA.\n");
                }
            }

            novaVenda = new VendaPrazo(dataHoje, codigoCliente, dataVencimento);

        } else {
            novaVenda = new VendaVista(dataHoje);
        }

        gerenciadorVendas.realizarVenda(novaVenda);
        System.out.println("Venda número " + novaVenda.getNumeroVenda() + " registrada com sucesso!");
        int codigoProdutoItem = -1;

        System.out.println("\n--- ADICIONANDO PRODUTOS À VENDA ---");

        while (codigoProdutoItem != 0) {
            System.out.print("Digite o código do produto (ou 0 para finalizar a venda): ");

            try {
                codigoProdutoItem = Integer.parseInt(scanner.nextLine());

                if (codigoProdutoItem == 0) {
                    System.out.println("Itens registrados. Venda finalizada com sucesso!\n");
                    break;
                }

                Produto produtoSendoVendido = gerenciadorProdutos.buscarProduto(codigoProdutoItem);
                System.out.println("Produto: " + produtoSendoVendido.getDescricaoProduto());
                System.out.println("Valor Unitário: R$ " + produtoSendoVendido.getValorVenda());

                System.out.print("Digite a quantidade: ");
                int quantidade = Integer.parseInt(scanner.nextLine());

                gerenciadorProdutos.baixarEstoque(codigoProdutoItem, quantidade);

                ItemVenda novoItem = new ItemVenda(novaVenda.getNumeroVenda(), codigoProdutoItem, quantidade, produtoSendoVendido.getValorVenda());
                gerenciadorVendas.registrarItemVenda(novoItem);

                System.out.println("Produto adicionado à venda!");

            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números válidos para o código e quantidade.\n");
            } catch (EntidadeNaoEncontradaException | RegraNegocioException e) {
                System.out.println("Erro: " + e.getMessage() + "\n");
            }
        }
    }

    private void menuConsultas() {
        int opcaocons = -1;

        while (opcaocons != 0) {
            System.out.println("\n=========================");
            System.out.println("    MENU DE CONSULTAS    ");
            System.out.println("=========================");
            System.out.println("1. Consultar Vendas por Período");
            System.out.println("2. Consultar Produtos com Estoque abaixo do mínimo");
            System.out.println("0. Voltar ao Menu Anterior");
            System.out.print("Escolha uma opção: ");

            try {
                opcaocons = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
                continue;
            }

            switch (opcaocons) {
                case 1:
                    String dataInicialInput = "";
                    String dataFinalInput = "";
                    DateTimeFormatter formatoValidacao = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    boolean datasValidas = false;

                    while (!datasValidas) {
                        try {
                            System.out.print("Digite a data inicial (DD/MM/AAAA): ");
                            dataInicialInput = scanner.nextLine().trim();
                            LocalDate.parse(dataInicialInput, formatoValidacao);

                            System.out.print("Digite a data final (DD/MM/AAAA): ");
                            dataFinalInput = scanner.nextLine().trim();
                            LocalDate.parse(dataFinalInput, formatoValidacao);

                            datasValidas = true;

                        } catch (DateTimeParseException e) {
                            System.out.println("Erro: Uma das datas digitadas é inválida ou não segue o padrão DD/MM/AAAA. Tente novamente.\n");
                        }
                    }

                    List<Venda> vendasEncontradas = gerenciadorVendas.consultarVendasPorPeriodo(dataInicialInput, dataFinalInput);
                    exibirRelatorioVendas(vendasEncontradas);
                    break;

                case 2:
                    List<Produto> produtosComEstoqueBaixo = gerenciadorProdutos.consultarEstoqueBaixo();
                    exibirRelatorioEstoqueBaixo(produtosComEstoqueBaixo);
                    break;

                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;

                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
                    break;
            }
        }
    }

    private void exibirRelatorioVendas(List<Venda> vendas) {
        System.out.println("\n==================================================");
        System.out.println("               RELATÓRIO DE VENDAS                ");
        System.out.println("==================================================");
        System.out.println("Nº VENDA | DATA | TIPO | CLIENTE | VENCIMENTO");
        System.out.println("--------------------------------------------------");

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada no período selecionado.");
            return;
        }

        for (Venda v : vendas) {
            System.out.println(v.getNumeroVenda() + " | " + v.getDataVenda() + " | " +
                    v.getTipoVenda() + " | " +
                    (v.getCodigoCliente() == -1 ? "N/A" : v.getCodigoCliente()) + " | " +
                    v.getDataVencimento());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Total de vendas no período: " + vendas.size());
    }

    private void exibirRelatorioEstoqueBaixo(List<Produto> produtos) {
        System.out.println("\n==================================================");
        System.out.println("       PRODUTOS COM ESTOQUE ABAIXO DO MÍNIMO      ");
        System.out.println("==================================================");
        System.out.println("CÓDIGO | DESCRIÇÃO | ATUAL | MÍNIMO");
        System.out.println("--------------------------------------------------");

        if (produtos.isEmpty()) {
            System.out.println("Excelente! Todos os produtos estão com estoque regular.");
            return;
        }

        for (Produto p : produtos) {
            System.out.println(p.getCodigoProduto() + " | " +
                    p.getDescricaoProduto() + " | " +
                    p.getEstoqueAtual() + " | " +
                    p.getEstoqueMinimo());
        }
    }
}