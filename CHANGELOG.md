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