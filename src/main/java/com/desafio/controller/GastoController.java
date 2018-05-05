package com.desafio.controller;

import com.desafio.exception.ResourceNotFoundException;
import com.desafio.model.Gasto;
import com.desafio.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;

@RestController
@RequestMapping("/api")
public class GastoController {

    private final GastoRepository gastoRepository;

    @Autowired
    public GastoController(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    @GetMapping("/gastos")
    public List<Gasto> buscaTodosGastos() {
        return gastoRepository.findAll();
    }

    @PostMapping("/gastos")
    public Gasto criaGasto(@Valid @RequestBody Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    @GetMapping("/gastos/{id}")
    public Gasto buscaGastoPorId(@PathVariable(value = "id") Long gastoId) {
        return gastoRepository.findById(gastoId)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", gastoId));
    }

    @PutMapping("/gastos/{id}")
    public Gasto atualizaGasto(@PathVariable(value = "id") Long gastoId,
                               @Valid @RequestBody Gasto novoGasto) {

        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", gastoId));

        gasto.setValor(novoGasto.getValor());
        gasto.setCategoria(novoGasto.getCategoria());

        return gastoRepository.save(gasto);
    }

    @DeleteMapping("/gastos/{id}")
    public ResponseEntity<?> deletaGasto(@PathVariable(value = "id") Long gastoId) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", gastoId));

        gastoRepository.delete(gasto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/gastos/resumo")
    public Map<String, DoubleSummaryStatistics> resumeGastosPorCategoria() {
        return gastoRepository
                .findAll()
                .stream()
                .collect(groupingBy(Gasto::getCategoria, summarizingDouble(Gasto::getValor)));
    }
}
