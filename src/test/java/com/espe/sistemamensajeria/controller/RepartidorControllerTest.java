
package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.entity.Repartidor;
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

@WebMvcTest(RepartidorController.class)
class RepartidorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepartidorService repartidorService;

    @Test
    void crearRepartidorDebeRetornar201() throws Exception {
        when(repartidorService.guardar(any(Repartidor.class)))
                .thenReturn(crearRepartidor());

        mockMvc.perform(post("/api/v1/repartidores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Carlos Ramirez",
                                  "telefono": "0987654321",
                                  "disponible": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Carlos Ramirez"))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void listarRepartidoresDebeRetornar200() throws Exception {
        when(repartidorService.listarTodos())
                .thenReturn(List.of(crearRepartidor()));

        mockMvc.perform(get("/api/v1/repartidores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Carlos Ramirez"));
    }

    @Test
    void obtenerRepartidorDebeRetornar200() throws Exception {
        when(repartidorService.buscarPorId(1L))
                .thenReturn(Optional.of(crearRepartidor()));

        mockMvc.perform(get("/api/v1/repartidores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos Ramirez"));
    }

    @Test
    void actualizarRepartidorDebeRetornar200() throws Exception {
        Repartidor actualizado = crearRepartidor();
        actualizado.setNombre("Carlos Actualizado");

        when(repartidorService.actualizar(eq(1L), any(Repartidor.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/repartidores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Carlos Actualizado",
                                  "telefono": "0987654321",
                                  "disponible": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos Actualizado"))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void eliminarRepartidorDebeRetornar204() throws Exception {
        when(repartidorService.buscarPorId(1L))
                .thenReturn(Optional.of(crearRepartidor()));

        mockMvc.perform(delete("/api/v1/repartidores/1"))
                .andExpect(status().isNoContent());

        verify(repartidorService).eliminar(1L);
    }

    @Test
    void crearRepartidorConDatosInvalidosDebeRetornar400() throws Exception {
        mockMvc.perform(post("/api/v1/repartidores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "telefono": "",
                                  "disponible": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(repartidorService, never()).guardar(any(Repartidor.class));
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
