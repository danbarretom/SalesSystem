import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GerenciadorProdutos gerenciadorProdutos = new GerenciadorProdutos();
        GerenciadorVendas gerenciadorVendas = new GerenciadorVendas();
        GerenciadorClientes gerenciadorClientes = new GerenciadorClientes();

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
                    menuProdutos(scanner, gerenciadorProdutos, gerenciadorVendas);
                    break;
                case 2:
                    menuClientes(scanner, gerenciadorClientes, gerenciadorVendas);
                    break;
                case 3:
                    menuVendas(scanner, gerenciadorVendas, gerenciadorProdutos, gerenciadorClientes);
                    break;
                case 4:
                    menuConsultas(scanner, gerenciadorVendas, gerenciadorProdutos);
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

    private static void menuProdutos(Scanner scanner, GerenciadorProdutos gerenciadorProdutos, GerenciadorVendas gerenciadorVendas) {
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

                    gerenciadorProdutos.cadastrarNovoProduto(novoProduto);
                    break;

                case 2:
                    System.out.println("\n--- CONSULTAR PRODUTO ---");
                    System.out.print("Digite o código do produto: ");
                    try {
                        int codigoConsulta = Integer.parseInt(scanner.nextLine());
                        gerenciadorProdutos.consultarProduto(codigoConsulta);
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\n--- ALTERAR PRODUTO ---");
                    System.out.print("Digite o código do produto que deseja alterar: ");
                    try {
                        int codigoAlterar = Integer.parseInt(scanner.nextLine());
                        gerenciadorProdutos.consultarProduto(codigoAlterar);

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

                    } catch (Exception e) {
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
                        }
                    } catch (Exception e) {
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

    private static void menuClientes(Scanner scanner, GerenciadorClientes gerenciadorClientes, GerenciadorVendas gerenciadorVendas) {
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
                    gerenciadorClientes.cadastrarNovoCliente(novoCliente);
                    break;

                case 2:
                    System.out.println("\n--- CONSULTAR CLIENTE ---");
                    System.out.print("Digite o código do cliente: ");
                    try {
                        int codigoConsulta = Integer.parseInt(scanner.nextLine());
                        gerenciadorClientes.consultarCliente(codigoConsulta);
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\n--- ALTERAR CLIENTE ---");
                    System.out.print("Digite o código do cliente que deseja alterar: ");
                    try {
                        int codigoAlterar = Integer.parseInt(scanner.nextLine());
                        System.out.println("Cliente atual:");
                        gerenciadorClientes.consultarCliente(codigoAlterar);

                        System.out.println("\nDigite os NOVOS dados do cliente:");
                        Cliente clienteAlterado = new Cliente();
                        System.out.print("Novo nome: ");
                        clienteAlterado.setNomeCliente(scanner.nextLine());
                        System.out.print("Novo Endereço: ");
                        clienteAlterado.setEnderecoCliente(scanner.nextLine());
                        System.out.print("Novo telefone: ");
                        clienteAlterado.setTelefoneCliente(scanner.nextLine());

                        gerenciadorClientes.alterarCliente(codigoAlterar, clienteAlterado);
                    } catch (Exception e) {
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
                        }
                    } catch (Exception e) {
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

    private static void menuVendas(Scanner scanner, GerenciadorVendas gerenciadorVendas, GerenciadorProdutos gerenciadorProdutos, GerenciadorClientes gerenciadorClientes) {
        System.out.println("\n======================================");
        System.out.println("         REALIZAR NOVA VENDA          ");
        System.out.println("======================================");

        String tipoVenda = "";
        boolean tipoValido = false;

        while (!tipoValido) {
            System.out.print("Qual o tipo da venda? (VISTA ou PRAZO): ");
            tipoVenda = scanner.nextLine().trim().toUpperCase();

            if (tipoVenda.equals("VISTA") || tipoVenda.equals("PRAZO")) {
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
                } catch (Exception e) {
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

        try {
            gerenciadorVendas.realizarVenda(novaVenda);
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
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage() + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro crítico ao tentar registrar a venda: " + e.getMessage());
        }
    }

    private static void menuConsultas(Scanner scanner, GerenciadorVendas gerenciadorVendas, GerenciadorProdutos gerenciadorProdutos) {
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

                    gerenciadorVendas.consultarVendasPorPeriodo(dataInicialInput, dataFinalInput);
                    break;

                case 2:
                    gerenciadorProdutos.consultarEstoqueBaixo();
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
}