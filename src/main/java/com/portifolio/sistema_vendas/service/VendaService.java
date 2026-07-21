package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.exception.RecursoNaoEncontradoException;
import com.portifolio.sistema_vendas.exception.RegraNegocioException;
import com.portifolio.sistema_vendas.model.Cliente;
import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
import com.portifolio.sistema_vendas.repository.ClienteRepository;
import com.portifolio.sistema_vendas.repository.ProdutoRepository;
import com.portifolio.sistema_vendas.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Venda salvarVenda(Venda venda) {
        if (venda.getTipoVenda() == TipoVenda.A_PRAZO) {
            resolverClienteParaVendaAPrazo(venda);
        }

        venda.setDataVenda(LocalDate.now());
        venda.setValorTotal(processarItens(venda));

        return vendaRepository.save(venda);
    }

    private void resolverClienteParaVendaAPrazo(Venda venda) {
        if (venda.getCliente() == null || venda.getDataVencimento() == null) {
            throw new RegraNegocioException("Vendas a prazo exigem Cliente e Data de Vencimento!");
        }

        Cliente clienteBanco = clienteRepository.findById(venda.getCliente().getCodigoCliente())
                .orElseThrow(() -> RecursoNaoEncontradoException.paraId("Cliente", venda.getCliente().getCodigoCliente()));
        venda.setCliente(clienteBanco);
    }

    private BigDecimal processarItens(Venda venda) {
        BigDecimal totalDaVenda = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {
            Produto produtoBanco = resolverProdutoComEstoqueDisponivel(item);

            // Reatribui a referência gerenciada: o Produto que chega no item pode estar detached,
            // e o decremento de estoque abaixo só é persistido via dirty-checking do Hibernate
            // se a entidade associada for a instância gerenciada, não a do request.
            item.setProduto(produtoBanco);

            BigDecimal subtotalItem = produtoBanco.getValorVenda().multiply(new BigDecimal(item.getQuantidade()));
            item.setSubtotal(subtotalItem);
            item.setVenda(venda);

            totalDaVenda = totalDaVenda.add(subtotalItem);
        }

        return totalDaVenda;
    }

    private Produto resolverProdutoComEstoqueDisponivel(ItemVenda item) {
        Produto produtoBanco = produtoRepository.findById(item.getProduto().getCodigoProduto())
                .orElseThrow(() -> RecursoNaoEncontradoException.paraId("Produto", item.getProduto().getCodigoProduto()));

        if (produtoBanco.getEstoqueAtual() < item.getQuantidade()) {
            throw new RegraNegocioException("Estoque insuficiente para o produto: " + produtoBanco.getDescricaoProduto() +
                    ". Quantidade disponível: " + produtoBanco.getEstoqueAtual());
        }
        produtoBanco.setEstoqueAtual(produtoBanco.getEstoqueAtual() - item.getQuantidade());

        return produtoBanco;
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public List<Venda> buscarVendasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }
}
