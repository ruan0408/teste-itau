package com.desafio.controller;

import com.desafio.model.Gasto;
import com.desafio.service.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GastoController {

    private final GastoService gastoService;

    @Autowired
    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping("/gastos")
    public List<Gasto> buscaTodosGastos() {
        return gastoService.buscaTodosGastos();
    }

    @PostMapping("/gastos")
    public Gasto criaGasto(@Valid @RequestBody Gasto gasto) {
        return gastoService.criaGasto(gasto);
    }

    @GetMapping("/gastos/{id}")
    public Gasto buscaGastoPorId(@PathVariable(value = "id") Long gastoId) {
        return gastoService.buscaGastoPorId(gastoId);
    }

    @PutMapping("/gastos/{id}")
    public Gasto atualizaGasto(@PathVariable(value = "id") Long gastoId,
                               @Valid @RequestBody Gasto novoGasto) {
        return gastoService.atualizaGasto(gastoId, novoGasto);
    }

    @DeleteMapping("/gastos/{id}")
    public ResponseEntity<?> deletaGasto(@PathVariable(value = "id") Long gastoId) {
        this.gastoService.deletaGasto(gastoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/gastos/resumo")
    public Map<String, DoubleSummaryStatistics> resumeGastosPorCategoria() {
        return this.gastoService.resumeGastosPorCategoria();
    }
}
