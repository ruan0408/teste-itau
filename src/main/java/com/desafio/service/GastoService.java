package com.desafio.service;

import com.desafio.exception.ResourceNotFoundException;
import com.desafio.model.Gasto;
import com.desafio.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;

@Service
public class GastoService {

    private GastoRepository gastoRepository;

    @Autowired
    public GastoService(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    public List<Gasto> buscaTodosGastos() {
        return this.gastoRepository.findAll();
    }

    public Gasto criaGasto(Gasto gasto) {
        return this.gastoRepository.save(gasto);
    }

    public Gasto buscaGastoPorId(Long id) {
        return this.gastoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id));
    }

    public Gasto atualizaGasto(Long id, Gasto novoGasto) {
        Gasto gasto = this.gastoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id));

        gasto.setValor(novoGasto.getValor());
        gasto.setCategoria(novoGasto.getCategoria());

        return this.gastoRepository.save(gasto);
    }

    public void deletaGasto(Long id) {
        Gasto gasto = this.gastoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id));

        this.gastoRepository.delete(gasto);
    }

    public Map<String, DoubleSummaryStatistics> resumeGastosPorCategoria() {
        return gastoRepository
                .findAll()
                .stream()
                .collect(groupingBy(Gasto::getCategoria, summarizingDouble(Gasto::getValor)));
    }
}
