
package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.entity.Entrega;
import com.espe.sistemamensajeria.entity.Paquete;
import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.service.EntregaService;
import com.espe.sistemamensajeria.service.PaqueteService;
import com.espe.sistemamensajeria.service.RepartidorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EntregaController.class)
class EntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EntregaService entregaService;

    @MockitoBean
    private PaqueteService paqueteService;

    @MockitoBean
    private RepartidorService repartidorService;

    @Test
    void crearEntregaDebeRetornar201() throws Exception {
        when(paqueteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearPaquete()));

        when(repartidorService.buscarPorId(1L))
                .thenReturn(Optional.of(crearRepartidor()));

        when(entregaService.guardar(any(Entrega.class)))
                .thenReturn(crearEntrega());

        mockMvc.perform(post("/api/v1/entregas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "estado": "PENDIENTE",
                                  "paqueteId": 1,
                                  "repartidorId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.paqueteId").value(1));
    }

    @Test
    void listarEntregasDebeRetornar200() throws Exception {
        when(entregaService.listarTodas())
                .thenReturn(List.of(crearEntrega()));

        mockMvc.perform(get("/api/v1/entregas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void listarPendientesDebeRetornar200() throws Exception {
        when(entregaService.listarPendientes())
                .thenReturn(List.of(crearEntrega()));

        mockMvc.perform(get("/api/v1/entregas/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void obtenerEntregaDebeRetornar200() throws Exception {
        when(entregaService.buscarPorId(1L))
                .thenReturn(Optional.of(crearEntrega()));

        mockMvc.perform(get("/api/v1/entregas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizarEstadoDebeRetornar200() throws Exception {
        Entrega actualizada = crearEntrega();
        actualizada.setEstado("EN_CAMINO");

        when(entregaService.actualizarEstado(1L, "EN_CAMINO"))
                .thenReturn(actualizada);

        mockMvc.perform(patch("/api/v1/entregas/1/estado")
                        .param("estado", "EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_CAMINO"));
    }

    @Test
    void asignarRepartidorDebeRetornar200() throws Exception {
        when(repartidorService.buscarPorId(1L))
                .thenReturn(Optional.of(crearRepartidor()));

        Entrega actualizada = crearEntrega();

        when(entregaService.asignarRepartidor(eq(1L), any(Repartidor.class)))
                .thenReturn(actualizada);

        mockMvc.perform(patch("/api/v1/entregas/1/repartidor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repartidorId").value(1));
    }

    @Test
    void eliminarEntregaDebeRetornar204() throws Exception {
        when(entregaService.buscarPorId(1L))
                .thenReturn(Optional.of(crearEntrega()));

        mockMvc.perform(delete("/api/v1/entregas/1"))
                .andExpect(status().isNoContent());

        verify(entregaService).eliminar(1L);
    }

    @Test
    void crearEntregaConDatosInvalidosDebeRetornar400() throws Exception {
        mockMvc.perform(post("/api/v1/entregas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "estado": "",
                                  "paqueteId": null,
                                  "repartidorId": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(entregaService, never()).guardar(any(Entrega.class));
    }

    @Test
    void crearEntregaConPaqueteInexistenteDebeRetornar404() throws Exception {
        when(paqueteService.buscarPorId(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/entregas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "estado": "PENDIENTE",
                                  "paqueteId": 999,
                                  "repartidorId": null
                                }
                                """))
                .andExpect(status().isNotFound());

        verify(entregaService, never()).guardar(any(Entrega.class));
    }

    @Test
    void asignarRepartidorInexistenteDebeRetornar404() throws Exception {
        when(repartidorService.buscarPorId(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/entregas/1/repartidor/999"))
                .andExpect(status().isNotFound());

        verify(entregaService, never())
                .asignarRepartidor(eq(1L), any(Repartidor.class));
    }

    private Entrega crearEntrega() {
        Entrega entrega = new Entrega();
        entrega.setId(1L);
        entrega.setEstado("PENDIENTE");
        entrega.setPaquete(crearPaquete());
        entrega.setRepartidor(crearRepartidor());
        return entrega;
    }

    private Paquete crearPaquete() {
        Paquete paquete = new Paquete();
        paquete.setId(1L);
        paquete.setDescripcion("Caja de documentos");
        paquete.setPeso(2.5);
        paquete.setDireccionDestino("Quito");
        return paquete;
    }

    private Repartidor crearRepartidor() {
        Repartidor repartidor = new Repartidor();
        repartidor.setId(1L);
        repartidor.setNombre("Carlos Ramirez");
        repartidor.setTelefono("0987654321");
        repartidor.setDisponible(true);
        return repartidor;
    }
}
