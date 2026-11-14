
package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.entity.Cliente;
import com.espe.sistemamensajeria.entity.Paquete;
import com.espe.sistemamensajeria.service.ClienteService;
import com.espe.sistemamensajeria.service.PaqueteService;

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

@WebMvcTest(PaqueteController.class)
class PaqueteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaqueteService paqueteService;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void crearPaqueteDebeRetornar201() throws Exception {
        when(clienteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearCliente()));
        when(paqueteService.guardar(any(Paquete.class)))
                .thenReturn(crearPaquete());

        mockMvc.perform(post("/api/v1/paquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "Caja de documentos",
                                  "peso": 2.5,
                                  "direccionDestino": "Quito",
                                  "clienteId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcion").value("Caja de documentos"))
                .andExpect(jsonPath("$.peso").value(2.5))
                .andExpect(jsonPath("$.clienteId").value(1));
    }

    @Test
    void listarPaquetesDebeRetornar200() throws Exception {
        when(paqueteService.listarTodos())
                .thenReturn(List.of(crearPaquete()));

        mockMvc.perform(get("/api/v1/paquetes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion")
                        .value("Caja de documentos"));
    }

    @Test
    void obtenerPaqueteDebeRetornar200() throws Exception {
        when(paqueteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearPaquete()));

        mockMvc.perform(get("/api/v1/paquetes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizarPaqueteDebeRetornar200() throws Exception {
        when(clienteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearCliente()));

        Paquete actualizado = crearPaquete();
        actualizado.setDescripcion("Caja actualizada");

        when(paqueteService.actualizar(eq(1L), any(Paquete.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/paquetes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "Caja actualizada",
                                  "peso": 3.0,
                                  "direccionDestino": "Quito",
                                  "clienteId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion")
                        .value("Caja actualizada"));
    }

    @Test
    void eliminarPaqueteDebeRetornar204() throws Exception {
        when(paqueteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearPaquete()));

        mockMvc.perform(delete("/api/v1/paquetes/1"))
                .andExpect(status().isNoContent());

        verify(paqueteService).eliminar(1L);
    }

    @Test
    void crearPaqueteConDatosInvalidosDebeRetornar400() throws Exception {
        mockMvc.perform(post("/api/v1/paquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcion": "",
                                  "peso": -2,
                                  "direccionDestino": "",
                                  "clienteId": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(paqueteService, never()).guardar(any(Paquete.class));
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");
        return cliente;
    }

    private Paquete crearPaquete() {
        Paquete paquete = new Paquete();
        paquete.setId(1L);
        paquete.setDescripcion("Caja de documentos");
        paquete.setPeso(2.5);
        paquete.setDireccionDestino("Quito");
        paquete.setCliente(crearCliente());
        return paquete;
    }
}
