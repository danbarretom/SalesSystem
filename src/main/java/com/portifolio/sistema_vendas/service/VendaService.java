package com.portifolio.sistema_vendas.service;

import com.portifolio.sistema_vendas.model.ItemVenda;
import com.portifolio.sistema_vendas.model.Produto;
import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
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

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Venda salvarVenda(Venda venda) {
        if (venda.getTipoVenda() == TipoVenda.A_PRAZO) {
            if (venda.getCliente() == null || venda.getDataVencimento() == null) {
                throw new IllegalArgumentException("Vendas a prazo exigem Cliente e Data de Vencimento!");
            }
        }

        venda.setDataVenda(LocalDate.now());

        BigDecimal totalDaVenda = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {

            Produto produtoBanco = produtoRepository.findById(item.getProduto().getCodigoProduto())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado!"));

            // Validação de Estoque
            if (produtoBanco.getEstoqueAtual() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produtoBanco.getDescricaoProduto() +
                        ". Quantidade disponível: " + produtoBanco.getEstoqueAtual());
            }
            // Abate o estoque
            produtoBanco.setEstoqueAtual(produtoBanco.getEstoqueAtual() - item.getQuantidade());

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
