## [3.0.0] - 2026-07-21

### Adicionado
- Frontend em React 19 + TypeScript + Vite, consumindo a API REST existente, com Tailwind CSS para estilos e React Router para navegação.
- Telas de listagem, cadastro, edição e exclusão para Produto e Cliente; registro, listagem e filtro por período para Venda (à vista e a prazo), com expansão de linha mostrando os itens de cada venda.
- Tela de estoque baixo e um dashboard inicial com resumo do sistema (totais de produtos, clientes, vendas, estoque baixo e valor total vendido).
- Camada de tipos TypeScript e módulos de API espelhando os DTOs/endpoints do backend, com tratamento de erro consistente (`ApiError`).
- Suíte de testes do frontend com Vitest + React Testing Library, cobrindo funções puras, interação de UI e componentes com a API mockada.
- Job de CI (`testes-frontend`) rodando a suíte de testes e o build de produção do frontend em todo push/PR.
- Deploy de produção do frontend na Vercel.

### Alterado
- Backend ganhou configuração de CORS (`CorsConfig`), liberando as origens de desenvolvimento local e a URL de produção do frontend na Vercel — necessário para uma aplicação em outra origem conseguir chamar a API.
- `README.md` reescrito para refletir o sistema como full-stack (backend + frontend), com stack técnica, instruções de execução e histórico do projeto atualizados.

## [2.0.0] - 2026-07-17

### Adicionado
- Reescrita completa do sistema como API REST em Spring Boot 4.1 + Spring Data JPA, substituindo o console app Java SE original (que continua disponível nas tags `v1.0.0`/`v1.2.0`).
- Arquitetura em camadas (`controller`/`service`/`repository`/`dto`/`model`/`exception`), DTOs com Bean Validation e tratamento de erro centralizado via `GlobalExceptionHandler`.
- Persistência em PostgreSQL (Supabase), com H2 mantido só para a suíte de testes.
- Suíte de 71 testes em camadas: Mockito puro nos services, `@DataJpaTest` nos repositórios, `@WebMvcTest` nos controllers e um teste de integração completo.
- Documentação interativa da API via springdoc-openapi/Swagger UI.
- Deploy em produção via Docker no Render.
- Automação de release: merge de uma branch `release/*` em `main` passa a criar a tag e a GitHub Release automaticamente, lendo a versão direto deste changelog.

### Alterado
- CI (`ci-cd.yml`) passa a rodar a suíte de testes em Pull Requests para `dev`, além de `main`; branch protection em ambas passa a exigir esse check.

### Corrigido
- `VendaService` associava `ItemVenda`/`Venda` à entidade `Produto`/`Cliente` detached vinda do request em vez da entidade gerenciada pelo Hibernate.
- Exceções de binding do Spring MVC (parâmetro ausente, tipo inválido, corpo malformado) respondiam 500 em vez de 400.
- Rotas inexistentes respondiam 500 em vez de 404; o handler genérico de erro não registrava a stack trace no log.
- `springdoc-openapi` travado na versão `2.5.0`, incompatível com o Spring Framework 7 do Spring Boot 4.1 — atualizado para `3.0.3`.

## [1.2.0] - 2026-07-15

### Alterado
- Refatoração dos Gerenciadores para retornar dados em vez de imprimir diretamente no console, centralizando toda a exibição na `ViewSistema` (consistente com a separação Managers/View já declarada no `README.md`).

### Adicionado
- Testes de round-trip de persistência (`gerarLinhaDoObjeto`/`criarObjetoDaLinha`) para Produto, Cliente, VendaVista e VendaPrazo.
- Testes de fronteira e de "não encontrado" cobrindo `excluirProduto`, `alterarProduto`, `excluirCliente` e `alterarCliente` nos três Gerenciadores.

### Corrigido
- Asserção genérica demais em teste de estoque insuficiente, trocada por `RegraNegocioException` explícita.

## [1.1.1] - 2026-07-15

### Changed
- Reestruturação de diretórios para o padrão da comunidade e integração com Maven (`src/main/java` e `src/test/java`).
- Atualização do `README.md` com detalhamento da Arquitetura, qualidade de software (CI/CD) e Roadmap para Spring Boot.

## [1.1.0] - 2026-07-14

### Adicionado
- Implementação de testes unitários com JUnit 5 (100% de cobertura de classes).
- Adição de Custom Exceptions (EntidadeNaoEncontradaException, RegraNegocioException).

### Alterado
- Refatoração de Clean Code em todos os main.java.Gerenciadores.
- Aplicação do padrão Try-with-Resources para gerenciar recursos de arquivos.
- Isolamento da camada de visão (main.java.ViewSistema) do código fonte principal (main.java.Main).

### Corrigido
- Tratamento de exceções em tempo de execução, removendo poluição de 'throws'.

---

## [1.0.0] - 2026-07-14

### Adicionado
- Versão inicial do sistema.
- Lógica de persistência em arquivos de texto (.txt).
- Gerenciamento básico de Produtos, Clientes e Vendas (à vista e a prazo).
- Estrutura baseada em classes de main.java.Gerenciadores e main.java.Modelos (POO).