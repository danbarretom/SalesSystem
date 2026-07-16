package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.model.Venda;
import com.portifolio.sistema_vendas.service.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    // Injeção de dependência do Service
    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // Rota POST para registrar uma nova venda
    @PostMapping
    public ResponseEntity<Venda> registrarVenda(@RequestBody Venda venda) {
        // Manda para o Service fazer todos os cálculos e salvar
        Venda novaVenda = vendaService.salvarVenda(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
    }

    // Rota GET para listar todas as vendas realizadas
    @GetMapping
    public ResponseEntity<List<Venda>> listarVendas() {
        List<Venda> vendas = vendaService.listarTodas();
        return ResponseEntity.ok(vendas);
    }

    // Rota GET para buscar vendas por período
    // Exemplo de chamada: /api/vendas/periodo?inicio=2026-07-01&fim=2026-07-15
    @GetMapping("/periodo")
    public ResponseEntity<List<Venda>> buscarVendasPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {

        List<Venda> vendas = vendaService.buscarVendasPorPeriodo(inicio, fim);
        return ResponseEntity.ok(vendas);
    }
}
