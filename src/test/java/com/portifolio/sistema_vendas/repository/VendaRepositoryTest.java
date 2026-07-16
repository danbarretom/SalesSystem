package com.portifolio.sistema_vendas.repository;

import com.portifolio.sistema_vendas.model.TipoVenda;
import com.portifolio.sistema_vendas.model.Venda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VendaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VendaRepository vendaRepository;

    private Venda criarVenda(LocalDate dataVenda) {
        Venda venda = new Venda();
        venda.setDataVenda(dataVenda);
        venda.setTipoVenda(TipoVenda.A_VISTA);
        venda.setValorTotal(new BigDecimal("100.00"));
        return entityManager.persistAndFlush(venda);
    }

    @Test
    void findByDataVendaBetween_retornaApenasVendasNoIntervaloInformado() {
        criarVenda(LocalDate.of(2026, 1, 5));
        criarVenda(LocalDate.of(2026, 1, 15));
        criarVenda(LocalDate.of(2026, 2, 1));

        List<Venda> resultado = vendaRepository.findByDataVendaBetween(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertThat(resultado)
                .extracting(Venda::getDataVenda)
                .containsExactlyInAnyOrder(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 15));
    }

    @Test
    void findByDataVendaBetween_incluiOsLimitesDoIntervalo() {
        criarVenda(LocalDate.of(2026, 1, 1));
        criarVenda(LocalDate.of(2026, 1, 31));

        List<Venda> resultado = vendaRepository.findByDataVendaBetween(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertThat(resultado).hasSize(2);
    }

    @Test
    void findByDataVendaBetween_semVendasNoIntervalo_retornaListaVazia() {
        criarVenda(LocalDate.of(2026, 3, 1));

        List<Venda> resultado = vendaRepository.findByDataVendaBetween(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertThat(resultado).isEmpty();
    }
}
