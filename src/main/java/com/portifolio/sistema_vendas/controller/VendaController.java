package com.portifolio.sistema_vendas.controller;

import com.portifolio.sistema_vendas.dto.VendaRequest;
import com.portifolio.sistema_vendas.dto.VendaResponse;
import com.portifolio.sistema_vendas.model.Venda;
import com.portifolio.sistema_vendas.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponse> registrarVenda(@Valid @RequestBody VendaRequest request) {
        Venda novaVenda = vendaService.salvarVenda(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(VendaResponse.from(novaVenda));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> listarVendas() {
        List<VendaResponse> vendas = vendaService.listarTodas().stream()
                .map(VendaResponse::from)
                .toList();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<VendaResponse>> buscarVendasPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {

        List<VendaResponse> vendas = vendaService.buscarVendasPorPeriodo(inicio, fim).stream()
                .map(VendaResponse::from)
                .toList();
        return ResponseEntity.ok(vendas);
    }
}
