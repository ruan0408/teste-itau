package com.desafio.service;

import com.desafio.model.Gasto;
import com.desafio.repository.GastoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GastoServiceTest {

    @MockBean
    private GastoRepository gastoRepository;

    @Autowired
    private GastoService gastoService;

    @Test
    public void buscaTodosGastos() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(112.45);

        when(gastoRepository.findAll()).thenReturn(singletonList(gasto));
        List<Gasto> gastos = this.gastoService.buscaTodosGastos();
        assertTrue(gastos.size() == 1);
    }

    @Test
    public void criaGasto() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(112.45);

        when(gastoRepository.save(any(Gasto.class))).thenReturn(gasto);
        Gasto novoGasto = this.gastoService.criaGasto(gasto);
        assertTrue(novoGasto.getCategoria().equals(gasto.getCategoria()));
    }

    @Test
    public void buscaGastoPorId() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(112.45);

        when(gastoRepository.findById(anyLong())).thenReturn(Optional.of(gasto));

        Gasto gastoEncontrado = this.gastoService.buscaGastoPorId(1L);
        assertTrue(gastoEncontrado.getCategoria().equals(gasto.getCategoria()));
    }

    @Test
    public void atualizaGasto() throws Exception {
        Gasto oldGasto = new Gasto();
        oldGasto.setCategoria("transporte");
        oldGasto.setValor(112.45);

        Gasto novoGasto = new Gasto();
        novoGasto.setCategoria("alimentação");
        novoGasto.setValor(50.45);

        when(gastoRepository.findById(anyLong())).thenReturn(Optional.of(oldGasto));
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(i -> i.getArguments()[0]);

        Gasto gastoAtualizado = this.gastoService.atualizaGasto(1L, novoGasto);
        assertTrue(gastoAtualizado.getCategoria().equals(novoGasto.getCategoria()));
    }

    @Test
    public void deletaGasto() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("alimentação");
        gasto.setValor(50.45);

        when(gastoRepository.findById(anyLong())).thenReturn(Optional.of(gasto));
        doNothing().when(gastoRepository).delete(any(Gasto.class));

        gastoService.deletaGasto(1L);
    }

    @Test
    public void resumeGastosPorCategoria() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(50.45);

        when(gastoRepository.findAll()).thenReturn(singletonList(gasto));
        Map<String, DoubleSummaryStatistics> map =
                this.gastoService.resumeGastosPorCategoria();

        assertTrue(map.containsKey("transporte"));
    }
}
