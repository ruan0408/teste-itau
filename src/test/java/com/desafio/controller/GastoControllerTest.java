package com.desafio.controller;

import com.desafio.model.Gasto;
import com.desafio.service.GastoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GastoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GastoService gastoService;

    @Test
    public void buscaTodosGastos() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(112.45);

        when(gastoService.buscaTodosGastos()).thenReturn(singletonList(gasto));

        mvc.perform(get("/api/gastos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoria", is("transporte")));
    }

    @Test
    public void criaGasto() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(112.45);

        String json = mapper.writeValueAsString(gasto);
        when(gastoService.criaGasto(any(Gasto.class))).thenReturn(gasto);

        mvc.perform(post("/api/gastos").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria", is("transporte")));
    }

    @Test
    public void buscaGastoPorId() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(112.45);

        when(gastoService.buscaGastoPorId(anyLong())).thenReturn(gasto);

        mvc.perform(get("/api/gastos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria", is("transporte")));
    }

    @Test
    public void atualizaGasto() throws Exception {
        Gasto oldGasto = new Gasto();
        oldGasto.setCategoria("transporte");
        oldGasto.setValor(112.45);

        Gasto newGasto = new Gasto();
        newGasto.setCategoria("alimentação");
        newGasto.setValor(50.45);

        String json = mapper.writeValueAsString(newGasto);

        when(gastoService.atualizaGasto(anyLong(), any(Gasto.class))).thenReturn(newGasto);

        mvc.perform(put("/api/gastos/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria", is("alimentação")));
    }

    @Test
    public void deletaGasto() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("alimentação");
        gasto.setValor(50.45);

        doNothing().when(gastoService).deletaGasto(anyLong());

        mvc.perform(delete("/api/gastos/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void resumeGastosPorCategoria() throws Exception {
        Gasto gasto = new Gasto();
        gasto.setCategoria("transporte");
        gasto.setValor(50.45);

        DoubleSummaryStatistics resumo = new DoubleSummaryStatistics();
        resumo.accept(gasto.getValor());

        Map<String, DoubleSummaryStatistics> map = new HashMap<>();
        map.put("transporte", resumo);

        when(gastoService.resumeGastosPorCategoria()).thenReturn(map);

        mvc.perform(get("/api/gastos/resumo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transporte.count", is(1)));
    }
}
