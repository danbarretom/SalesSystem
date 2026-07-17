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
            if (venda.getCliente() == null || venda.getDataVencimento() == null) {
                throw new RegraNegocioException("Vendas a prazo exigem Cliente e Data de Vencimento!");
            }

            Cliente clienteBanco = clienteRepository.findById(venda.getCliente().getCodigoCliente())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o código: " + venda.getCliente().getCodigoCliente()));
            venda.setCliente(clienteBanco);
        }

        venda.setDataVenda(LocalDate.now());

        BigDecimal totalDaVenda = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {

            Produto produtoBanco = produtoRepository.findById(item.getProduto().getCodigoProduto())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o código: " + item.getProduto().getCodigoProduto()));

            // Validação de Estoque
            if (produtoBanco.getEstoqueAtual() < item.getQuantidade()) {
                throw new RegraNegocioException("Estoque insuficiente para o produto: " + produtoBanco.getDescricaoProduto() +
                        ". Quantidade disponível: " + produtoBanco.getEstoqueAtual());
            }
            // Abate o estoque
            produtoBanco.setEstoqueAtual(produtoBanco.getEstoqueAtual() - item.getQuantidade());

            // Garante que o item persistido referencia a entidade gerenciada, não o objeto parcial vindo da requisição
            item.setProduto(produtoBanco);

            // Calcula o subtotal (Preço do banco x Quantidade)
            BigDecimal subtotalItem = produtoBanco.getValorVenda().multiply(new BigDecimal(item.getQuantidade()));
            item.setSubtotal(subtotalItem);
            item.setVenda(venda);

            // Soma o subtotal do item ao Total Geral da Venda
            totalDaVenda = totalDaVenda.add(subtotalItem);
        }

        // 4. Salva o valor total calculado na venda
        venda.setValorTotal(totalDaVenda);

        // 5. Salva tudo de uma vez
        return vendaRepository.save(venda);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    // Repassa a chamada para o Repository filtrar as datas
    public List<Venda> buscarVendasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }
}
